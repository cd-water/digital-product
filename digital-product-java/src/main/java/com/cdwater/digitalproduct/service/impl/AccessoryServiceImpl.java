package com.cdwater.digitalproduct.service.impl;

import com.alibaba.fastjson2.JSON;
import com.cdwater.digitalproduct.common.constants.RedisMark;
import com.cdwater.digitalproduct.common.constants.RoleType;
import com.cdwater.digitalproduct.common.utils.RedisUtil;
import com.cdwater.digitalproduct.common.utils.ThreadUtil;
import com.cdwater.digitalproduct.entity.Accessory;
import com.cdwater.digitalproduct.mapper.CartMapper;
import com.cdwater.digitalproduct.mapper.AccessoryMapper;
import com.cdwater.digitalproduct.model.dto.AccessoryPageDTO;
import com.cdwater.digitalproduct.model.vo.AccessoryPageVO;
import com.cdwater.digitalproduct.model.vo.AccessoryQueryVO;
import com.cdwater.digitalproduct.model.vo.AccessoryShowVO;
import com.cdwater.digitalproduct.service.AccessoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class AccessoryServiceImpl implements AccessoryService {

    @Resource
    private AccessoryMapper accessoryMapper;
    @Resource
    private CartMapper cartMapper;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public void add(Accessory accessory) {
        //设置数码配件所属店铺
        accessory.setShopId(ThreadUtil.getId());
        accessoryMapper.insert(accessory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeOne(Integer id) {
        //操作数据库
        accessoryMapper.deleteById(id);
        cartMapper.deleteByAccessoryId(id);

        //判断是否删除到热门数码配件
        List<AccessoryShowVO> listCache = redisUtil.getListByHashValues(RedisMark.ACCESSORY_HOT_KEY, AccessoryShowVO.class);
        if (listCache != null) {
            //删除到热门数码配件，则清理缓存
            for (AccessoryShowVO item : listCache) {
                if (item.getId().equals(id)) {
                    redisUtil.delete(RedisMark.ACCESSORY_HOT_KEY);
                    break;
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatch(List<Integer> ids) {
        //操作数据库
        accessoryMapper.deleteByIds(ids);
        cartMapper.deleteByAccessoryIds(ids);

        //判断是否删除到热门数码配件
        List<AccessoryShowVO> listCache = redisUtil.getListByHashValues(RedisMark.ACCESSORY_HOT_KEY, AccessoryShowVO.class);
        if (listCache != null) {
            Set<Integer> idsSet = new HashSet<>(ids);
            //删除到热门数码配件，则清理缓存
            for (AccessoryShowVO item : listCache) {
                if (idsSet.contains(item.getId())) {
                    redisUtil.delete(RedisMark.ACCESSORY_HOT_KEY);
                    break;
                }
            }
        }
    }

    @Override
    public void edit(Accessory accessory) {
        //操作数据库
        accessoryMapper.updateById(accessory);

        //判断是否修改到热门数码配件
        List<AccessoryShowVO> listCache = redisUtil.getListByHashValues(RedisMark.ACCESSORY_HOT_KEY, AccessoryShowVO.class);
        if (listCache != null) {
            //修改到热门数码配件，则清理缓存
            listCache.forEach(item -> {
                if (item.getId().equals(accessory.getId())) {
                    redisUtil.delete(RedisMark.ACCESSORY_HOT_KEY);
                }
            });
        }
    }

    @Override
    public AccessoryQueryVO query(Integer id) {
        return accessoryMapper.selectById(id);
    }

    @Override
    public PageInfo<AccessoryPageVO> page(AccessoryPageDTO accessoryPageDTO) {
        //是否限定店铺范围
        if (RoleType.isDigitalShop(ThreadUtil.getRole())) {
            accessoryPageDTO.setShopId(ThreadUtil.getId());
        }
        PageHelper.startPage(accessoryPageDTO.getPageNum(), accessoryPageDTO.getPageSize());
        List<AccessoryPageVO> list = accessoryMapper.selectPage(accessoryPageDTO);
        return PageInfo.of(list);
    }

    @Override
    public List<AccessoryShowVO> getHot() {
        //查询redis缓存
        String key = RedisMark.ACCESSORY_HOT_KEY;
        List<AccessoryShowVO> listCache = redisUtil.getListByHashValues(key, AccessoryShowVO.class);

        //命中缓存
        if (listCache != null) {
            return listCache;
        }

        //未命中缓存，查询数据库
        List<AccessoryShowVO> listDB = accessoryMapper.selectHot12();
        if (CollectionUtils.isNotEmpty(listDB)) {
            //回写redis缓存
            Map<String, String> mapSave = new HashMap<>();
            listDB.forEach(item -> mapSave.put(item.getId().toString(), JSON.toJSONString(item)));
            redisUtil.setHash(key, mapSave, RedisMark.ACCESSORY_HOT_TTL, TimeUnit.SECONDS, false);
        } else {
            //数据库无数据，缓存空标记，防止缓存穿透
            redisUtil.setHash(key, RedisMark.EMPTY_MAP, RedisMark.EMPTY_TTL, TimeUnit.SECONDS, false);
        }
        return listDB;
    }

    @Override
    public PageInfo<AccessoryShowVO> pagePublic(AccessoryPageDTO accessoryPageDTO) {
        PageHelper.startPage(accessoryPageDTO.getPageNum(), accessoryPageDTO.getPageSize());
        List<AccessoryShowVO> list = accessoryMapper.selectPagePublic(accessoryPageDTO);
        return PageInfo.of(list);
    }
}
