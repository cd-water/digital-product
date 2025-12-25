package com.cdwater.digitalproduct.service.impl;

import com.cdwater.digitalproduct.common.constants.RedisMark;
import com.cdwater.digitalproduct.common.enums.ReturnMsg;
import com.cdwater.digitalproduct.common.exception.BusinessException;
import com.cdwater.digitalproduct.common.utils.RedisUtil;
import com.cdwater.digitalproduct.mapper.AccessoryTypeMapper;
import com.cdwater.digitalproduct.entity.AccessoryType;
import com.cdwater.digitalproduct.service.AccessoryTypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AccessoryTypeServiceImpl implements AccessoryTypeService {

    @Resource
    private AccessoryTypeMapper accessoryTypeMapper;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public void add(AccessoryType accessoryType) {
        //类型不能重名
        AccessoryType accessoryTypeDB = accessoryTypeMapper.selectByName(accessoryType.getName());
        if (accessoryTypeDB != null) {
            //类型已存在
            throw new BusinessException(ReturnMsg.Type_EXISTED);
        }

        //操作数据库
        accessoryTypeMapper.insert(accessoryType);

        //清理缓存，保证数据一致性
        List<String> keys = new ArrayList<>();
        keys.add(RedisMark.ACCESSORY_TYPE_HOT_KEY);
        keys.add(RedisMark.ACCESSORY_TYPE_ALL_KEY);

        redisUtil.delete(keys);
    }

    @Override
    public void removeOne(Integer id) {
        //操作数据库
        accessoryTypeMapper.deleteById(id);

        //清理缓存，保证数据一致性
        List<String> keys = new ArrayList<>();
        keys.add(RedisMark.ACCESSORY_TYPE_HOT_KEY);
        keys.add(RedisMark.ACCESSORY_TYPE_ALL_KEY);
        redisUtil.delete(keys);
    }

    @Override
    public void removeBatch(List<Integer> ids) {
        //操作数据库
        accessoryTypeMapper.deleteByIds(ids);

        //清理缓存，保证数据一致性
        List<String> keys = new ArrayList<>();
        keys.add(RedisMark.ACCESSORY_TYPE_HOT_KEY);
        keys.add(RedisMark.ACCESSORY_TYPE_ALL_KEY);
        redisUtil.delete(keys);
    }

    @Override
    public void edit(AccessoryType accessoryType) {
        //类型不能重名
        AccessoryType accessoryTypeDB = accessoryTypeMapper.selectByName(accessoryType.getName());
        if (accessoryTypeDB != null) {
            //类型已存在
            throw new BusinessException(ReturnMsg.Type_EXISTED);
        }

        //操作数据库
        accessoryTypeMapper.updateById(accessoryType);

        //清理缓存，保证数据一致性
        List<String> keys = new ArrayList<>();
        keys.add(RedisMark.ACCESSORY_TYPE_HOT_KEY);
        keys.add(RedisMark.ACCESSORY_TYPE_ALL_KEY);
        redisUtil.delete(keys);
    }

    @Override
    public AccessoryType query(Integer id) {
        return accessoryTypeMapper.selectById(id);
    }

    @Override
    public PageInfo<AccessoryType> page(AccessoryType accessoryType, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<AccessoryType> list = accessoryTypeMapper.selectPage(accessoryType);
        return PageInfo.of(list);
    }

    @Override
    public List<AccessoryType> getAll() {
        //查询redis缓存
        String key = RedisMark.ACCESSORY_TYPE_ALL_KEY;
        List<AccessoryType> listCache = redisUtil.getListByJson(key, AccessoryType.class);

        //命中缓存
        if (listCache != null) {
            return listCache;
        }

        //未命中缓存，查询数据库
        List<AccessoryType> listDB = accessoryTypeMapper.selectAll();
        if (CollectionUtils.isNotEmpty(listDB)) {
            //回写redis缓存
            redisUtil.setJson(key, listDB, RedisMark.ACCESSORY_TYPE_ALL_TTL, TimeUnit.SECONDS, false);
        } else {
            //数据库无数据，缓存空标记，防止缓存穿透
            redisUtil.setStr(key, RedisMark.EMPTY, RedisMark.EMPTY_TTL, TimeUnit.SECONDS, false);
        }
        return listDB;
    }

    @Override
    public List<AccessoryType> getHot() {
        //查询redis缓存
        String key = RedisMark.ACCESSORY_TYPE_HOT_KEY;
        List<AccessoryType> listCache = redisUtil.getListByJson(key, AccessoryType.class);

        //命中缓存
        if (listCache != null) {
            return listCache;
        }

        //未命中缓存，查询数据库
        List<AccessoryType> listDB = accessoryTypeMapper.selectHot4();
        if (CollectionUtils.isNotEmpty(listDB)) {
            //回写redis缓存
            redisUtil.setJson(key, listDB, RedisMark.ACCESSORY_TYPE_HOT_TTL, TimeUnit.SECONDS, false);
        } else {
            //数据库无数据，缓存空标记，防止缓存穿透
            redisUtil.setStr(key, RedisMark.EMPTY, RedisMark.EMPTY_TTL, TimeUnit.SECONDS, false);
        }
        return listDB;
    }
}
