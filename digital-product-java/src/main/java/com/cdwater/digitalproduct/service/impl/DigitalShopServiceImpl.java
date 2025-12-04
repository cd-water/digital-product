package com.cdwater.digitalproduct.service.impl;

import com.alibaba.fastjson2.JSON;
import com.cdwater.digitalproduct.common.constants.*;
import com.cdwater.digitalproduct.common.enums.ReturnMsg;
import com.cdwater.digitalproduct.common.exception.BusinessException;
import com.cdwater.digitalproduct.common.utils.RedisUtil;
import com.cdwater.digitalproduct.common.utils.ThreadUtil;
import com.cdwater.digitalproduct.config.properties.JwtProperties;
import com.cdwater.digitalproduct.mapper.AccessoryMapper;
import com.cdwater.digitalproduct.mapper.ProductMapper;
import com.cdwater.digitalproduct.mapper.DigitalShopMapper;
import com.cdwater.digitalproduct.entity.DigitalShop;
import com.cdwater.digitalproduct.mapper.SlideshowMapper;
import com.cdwater.digitalproduct.model.dto.DigitalShopPageDTO;
import com.cdwater.digitalproduct.model.vo.AccessoryShowVO;
import com.cdwater.digitalproduct.model.vo.DigitalShopShowVO;
import com.cdwater.digitalproduct.model.vo.DigitalShopDetailVO;
import com.cdwater.digitalproduct.service.DigitalShopService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class DigitalShopServiceImpl implements DigitalShopService {

    @Resource
    private DigitalShopMapper digitalShopMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private AccessoryMapper accessoryMapper;
    @Resource
    private SlideshowMapper slideshowMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private JwtProperties jwtProperties;

    @Override
    public void add(DigitalShop digitalShop) {
        //查询账号是否存在，账号已存在, 则抛出异常
        DigitalShop digitalShopDBUsername = digitalShopMapper.selectByUsername(digitalShop.getUsername());
        if (digitalShopDBUsername != null) {
            throw new BusinessException(ReturnMsg.ACCOUNT_REGISTER);
        }
        //查询手机号是否存在，手机号已存在，则抛出异常
        DigitalShop digitalShopDBPhone = digitalShopMapper.selectByPhone(digitalShop.getPhone());
        if (digitalShopDBPhone != null) {
            throw new BusinessException(ReturnMsg.PHONE_REGISTER);
        }

        //明确角色
        digitalShop.setRole(RoleType.DIGITALSHOP);
        //默认密码md5加密存储
        digitalShop.setPassword(DigestUtils.md5DigestAsHex(TextInfo.DEFAULT_PASSWORD.getBytes()));

        //操作数据库
        digitalShopMapper.insert(digitalShop);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeOne(Integer id) {
        //查询店铺旗下所有数码产品的id，用于后续删除数码产品相关缓存
        List<Integer> productIds = productMapper.selectIdsByShopId(id);
        //查询店铺旗下所有数码配件的id，用于后续删除数码配件相关缓存
        List<Integer> accessoryIds = accessoryMapper.selectIdsByShopId(id);

        //操作数据库
        productMapper.deleteByShopId(id);//清空店铺下的数码产品
        accessoryMapper.deleteByShopId(id);//清空店铺下的数码配件
        slideshowMapper.deleteByShopId(id);//删除店铺旗下数码产品的轮播图
        digitalShopMapper.deleteById(id);//删除店铺

        //清理店铺相关缓存
        redisUtil.delete(RedisMark.DIGITAL_SHOP_DETAIL_PREFIX + id);

        //判断是否删除到热门店铺
        List<DigitalShopShowVO> listCache = redisUtil.getListByHashValues(RedisMark.DIGITAL_SHOP_HOT_KEY, DigitalShopShowVO.class);
        if (listCache != null) {
            //删除到热门店铺，则清理缓存
            for (DigitalShopShowVO item : listCache) {
                if (item.getId().equals(id)) {
                    redisUtil.delete(RedisMark.DIGITAL_SHOP_HOT_KEY);
                    break;
                }
            }
        }

        //清理旗下被删除数码产品详情缓存
        List<String> keys = new ArrayList<>();
        List<String> fields = new ArrayList<>();
        for (Integer productId : productIds) {
            keys.add(RedisMark.PRODUCT_DETAIL_PREFIX + productId);
            fields.add(productId.toString());
        }
        redisUtil.delete(keys);
        redisUtil.deleteHashValues(RedisMark.PRODUCT_RECOMMEND_KEY, fields);


        //判断是否删除到热门数码配件
        List<AccessoryShowVO> accessoryListCache = redisUtil.getListByHashValues(RedisMark.ACCESSORY_HOT_KEY, AccessoryShowVO.class);
        if (accessoryListCache != null) {
            Set<Integer> accessoryIdsSet = new HashSet<>(accessoryIds);
            //删除到热门数码配件，则清理缓存
            for (AccessoryShowVO item : accessoryListCache) {
                if (accessoryIdsSet.contains(item.getId())) {
                    redisUtil.delete(RedisMark.ACCESSORY_HOT_KEY);
                    break;
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatch(List<Integer> ids) {
        //查询所有店铺旗下所有的数码产品的id，用于后续删除数产品相关缓存
        List<Integer> productIds = productMapper.selectIdsByShopIds(ids);
        //查询所有店铺旗下所有的数码配件的id，用于后续删除数码配件相关缓存
        List<Integer> accessoryIds = accessoryMapper.selectIdsByShopIds(ids);

        //操作数据库
        productMapper.deleteByShopIds(ids);//清空店铺下的数码产品
        accessoryMapper.deleteByShopIds(ids);//清空店铺下的数码配件
        slideshowMapper.deleteByShopIds(ids);//删除店铺旗下数码产品的轮播图
        digitalShopMapper.deleteByIds(ids);//删除店铺

        //清理店铺相关缓存
        List<String> shopKeys = new ArrayList<>();
        for (Integer id : ids) {
            shopKeys.add(RedisMark.DIGITAL_SHOP_DETAIL_PREFIX + id);
        }
        redisUtil.delete(shopKeys);

        //判断是否删除到热门店铺
        List<DigitalShopShowVO> listCache = redisUtil.getListByHashValues(RedisMark.DIGITAL_SHOP_HOT_KEY, DigitalShopShowVO.class);
        if (listCache != null) {
            Set<Integer> shopIdsSet = new HashSet<>(ids);
            //删除到热门店铺，则清理缓存
            for (DigitalShopShowVO item : listCache) {
                if (shopIdsSet.contains(item.getId())) {
                    redisUtil.delete(RedisMark.DIGITAL_SHOP_HOT_KEY);
                    break;
                }
            }
        }

        //清理旗下被删除数码产品详情缓存
        List<String> productKeys = new ArrayList<>();
        List<String> fields = new ArrayList<>();
        for (Integer productId : productIds) {
            productKeys.add(RedisMark.PRODUCT_DETAIL_PREFIX + productId);
            fields.add(productId.toString());
        }
        redisUtil.delete(productKeys);
        redisUtil.deleteHashValues(RedisMark.PRODUCT_RECOMMEND_KEY, fields);


        //判断是否删除到热门数码配件
        List<AccessoryShowVO> accessoryListCache = redisUtil.getListByHashValues(RedisMark.ACCESSORY_HOT_KEY, AccessoryShowVO.class);
        if (accessoryListCache != null) {
            Set<Integer> accessoryIdsSet = new HashSet<>(accessoryIds);
            //删除到热门数码配件，则清理缓存
            for (AccessoryShowVO item : accessoryListCache) {
                if (accessoryIdsSet.contains(item.getId())) {
                    redisUtil.delete(RedisMark.ACCESSORY_HOT_KEY);
                    break;
                }
            }
        }
    }

    @Override
    public void edit(DigitalShop digitalShop, HttpServletRequest request) {
        //查询手机号是否存在，手机号已存在，则抛出异常
        DigitalShop digitalShopDB = digitalShopMapper.selectByPhone(digitalShop.getPhone());
        if (digitalShopDB != null && !StringUtils.equals(digitalShop.getUsername(), digitalShopDB.getUsername())) {
            throw new BusinessException(ReturnMsg.PHONE_REGISTER);
        }

        //操作数据库
        digitalShopMapper.updateById(digitalShop);

        //清理缓存，保证数据一致性
        redisUtil.delete(RedisMark.DIGITAL_SHOP_DETAIL_PREFIX + digitalShop.getId());

        //判断是否在修改自身信息，是则更新redis缓存
        if (ThreadUtil.hasPermission(digitalShop.getId(), RoleType.DIGITALSHOP)) {
            String token = request.getHeader(jwtProperties.getTokenName());
            String profileKey = RedisMark.PROFILE_PREFIX + token;
            redisUtil.updateHashAll(profileKey, digitalShop);
        }
    }

    @Override
    public DigitalShop query(Integer id, HttpServletRequest request) {
        //判断是否在查询自身信息，是则查询redis缓存
        if (ThreadUtil.hasPermission(id, RoleType.DIGITALSHOP)) {
            String token = request.getHeader(jwtProperties.getTokenName());
            String profileKey = RedisMark.PROFILE_PREFIX + token;
            DigitalShop digitalShopCache = redisUtil.getObjByHash(profileKey, DigitalShop.class);
            if (digitalShopCache != null) {
                return digitalShopCache;
            }
        }

        //查询非自身信息，查询数据库
        DigitalShop digitalShop = digitalShopMapper.selectById(id);
        //不回显密码
        digitalShop.setPassword(null);
        return digitalShop;
    }

    @Override
    public PageInfo<DigitalShop> page(DigitalShopPageDTO digitalShopPageDTO) {
        PageHelper.startPage(digitalShopPageDTO.getPageNum(), digitalShopPageDTO.getPageSize());
        List<DigitalShop> list = digitalShopMapper.selectPage(digitalShopPageDTO);
        return PageInfo.of(list);
    }

    @Override
    public List<DigitalShopShowVO> getHot() {
        //查询redis缓存
        String key = RedisMark.DIGITAL_SHOP_HOT_KEY;
        List<DigitalShopShowVO> listCache = redisUtil.getListByHashValues(key, DigitalShopShowVO.class);

        //命中缓存
        if (listCache != null) {
            return listCache;
        }

        //未命中缓存，查询数据库
        List<DigitalShopShowVO> listDB = digitalShopMapper.selectHot6();
        if (CollectionUtils.isNotEmpty(listDB)) {
            //回写redis缓存
            Map<String, String> mapSave = new HashMap<>();
            listDB.forEach(item -> mapSave.put(item.getId().toString(), JSON.toJSONString(item)));
            redisUtil.setHash(key, mapSave, RedisMark.DIGITAL_SHOP_HOT_TTL, TimeUnit.SECONDS, false);
        } else {
            //数据库无数据，缓存空标记，防止缓存穿透
            redisUtil.setHash(key, RedisMark.EMPTY_MAP, RedisMark.EMPTY_TTL, TimeUnit.SECONDS, false);
        }
        return listDB;
    }

    @Override
    public PageInfo<DigitalShopShowVO> pagePublic(DigitalShopPageDTO digitalShopPageDTO) {
        PageHelper.startPage(digitalShopPageDTO.getPageNum(), digitalShopPageDTO.getPageSize());
        List<DigitalShopShowVO> list = digitalShopMapper.selectPagePublic(digitalShopPageDTO);
        return PageInfo.of(list);
    }

    @Override
    public DigitalShopDetailVO detail(Integer id) {
        //查询redis缓存
        String key = RedisMark.DIGITAL_SHOP_DETAIL_PREFIX + id;
        DigitalShopDetailVO digitalShopDetailVOCache = redisUtil.getObjByHash(key, DigitalShopDetailVO.class);

        //命中缓存
        if (digitalShopDetailVOCache != null) {
            return digitalShopDetailVOCache;
        }

        //未命中缓存，查询数据库
        DigitalShopDetailVO digitalShopDetailVODB = digitalShopMapper.selectDetail(id);
        if (digitalShopDetailVODB != null) {
            //写入redis缓存
            redisUtil.setHash(key, digitalShopDetailVODB, RedisMark.DIGITAL_SHOP_DETAIL_TTL, TimeUnit.SECONDS, false);
        } else {
            //数据库无数据，缓存空对象，防止缓存穿透
            redisUtil.setHash(key, RedisMark.EMPTY_MAP, RedisMark.EMPTY_TTL, TimeUnit.SECONDS, false);
        }
        return digitalShopDetailVODB;
    }
}
