package com.cdwater.digitalproduct.service.impl;

import com.cdwater.digitalproduct.common.constants.RedisMark;
import com.cdwater.digitalproduct.common.enums.ReturnMsg;
import com.cdwater.digitalproduct.common.exception.BusinessException;
import com.cdwater.digitalproduct.common.utils.RedisUtil;
import com.cdwater.digitalproduct.mapper.ProductTypeMapper;
import com.cdwater.digitalproduct.entity.ProductType;
import com.cdwater.digitalproduct.service.ProductTypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {

    @Resource
    private ProductTypeMapper productTypeMapper;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public void add(ProductType productType) {
        //类型不能重名
        ProductType productTypeDB = productTypeMapper.selectByName(productType.getName());
        if (productTypeDB != null) {
            //类型已存在
            throw new BusinessException(ReturnMsg.Type_EXISTED);
        }

        //操作数据库
        productTypeMapper.insert(productType);

        //清理缓存，保证数据一致性
        List<String> keys = new ArrayList<>();
        keys.add(RedisMark.PRODUCT_TYPE_HOT_KEY);
        keys.add(RedisMark.PRODUCT_TYPE_ALL_KEY);
        redisUtil.delete(keys);
    }

    @Override
    public void removeOne(Integer id) {
        //操作数据库
        productTypeMapper.deleteById(id);

        //清理缓存，保证数据一致性
        List<String> keys = new ArrayList<>();
        keys.add(RedisMark.PRODUCT_TYPE_HOT_KEY);
        keys.add(RedisMark.PRODUCT_TYPE_ALL_KEY);

        redisUtil.delete(keys);
    }

    @Override
    public void removeBatch(List<Integer> ids) {
        //操作数据库
        productTypeMapper.deleteByIds(ids);

        //清理缓存，保证数据一致性
        List<String> keys = new ArrayList<>();
        keys.add(RedisMark.PRODUCT_TYPE_HOT_KEY);
        keys.add(RedisMark.PRODUCT_TYPE_ALL_KEY);
        redisUtil.delete(keys);
    }

    @Override
    public void edit(ProductType productType) {
        //类型不能重名
        ProductType productTypeDB = productTypeMapper.selectByName(productType.getName());
        if (productTypeDB != null && !Objects.equals(productTypeDB.getId(), productType.getId())) {
            //类型已存在
            throw new BusinessException(ReturnMsg.Type_EXISTED);
        }

        //操作数据库
        productTypeMapper.updateById(productType);

        //清理缓存，保证数据一致性
        List<String> keys = new ArrayList<>();
        keys.add(RedisMark.PRODUCT_TYPE_HOT_KEY);
        keys.add(RedisMark.PRODUCT_TYPE_ALL_KEY);
        redisUtil.delete(keys);
    }

    @Override
    public ProductType query(Integer id) {
        return productTypeMapper.selectById(id);
    }

    @Override
    public PageInfo<ProductType> page(ProductType productType, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProductType> list = productTypeMapper.selectPage(productType);
        return PageInfo.of(list);
    }

    @Override
    public List<ProductType> getAll() {
        //查询redis缓存
        String key = RedisMark.PRODUCT_TYPE_ALL_KEY;
        List<ProductType> listCache = redisUtil.getListByJson(key, ProductType.class);

        //命中缓存
        if (listCache != null) {
            return listCache;
        }

        //未命中缓存，查询数据库
        List<ProductType> listDB = productTypeMapper.selectAll();
        if (CollectionUtils.isNotEmpty(listDB)) {
            //回写redis缓存
            redisUtil.setJson(key, listDB, RedisMark.PRODUCT_TYPE_ALL_TTL, TimeUnit.SECONDS, false);
        } else {
            //数据库无数据，缓存空标记，防止缓存穿透
            redisUtil.setStr(key, RedisMark.EMPTY, RedisMark.EMPTY_TTL, TimeUnit.SECONDS, false);
        }
        return listDB;
    }

    @Override
    public List<ProductType> getHot() {
        //查询redis缓存
        String key = RedisMark.PRODUCT_TYPE_HOT_KEY;
        List<ProductType> listCache = redisUtil.getListByJson(key, ProductType.class);

        //命中缓存
        if (listCache != null) {
            return listCache;
        }

        //未命中缓存，查询数据库
        List<ProductType> listDB = productTypeMapper.selectHot4();
        if (CollectionUtils.isNotEmpty(listDB)) {
            //回写redis缓存
            redisUtil.setJson(key, listDB, RedisMark.PRODUCT_TYPE_HOT_TTL, TimeUnit.SECONDS, false);
        } else {
            //数据库无数据，缓存空标记，防止缓存穿透
            redisUtil.setStr(key, RedisMark.EMPTY, RedisMark.EMPTY_TTL, TimeUnit.SECONDS, false);
        }
        return listDB;
    }
}
