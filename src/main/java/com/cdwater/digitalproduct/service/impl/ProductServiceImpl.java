package com.cdwater.digitalproduct.service.impl;

import com.alibaba.fastjson2.JSON;
import com.cdwater.digitalproduct.common.constants.RedisMark;
import com.cdwater.digitalproduct.common.constants.RoleType;
import com.cdwater.digitalproduct.common.enums.ReturnMsg;
import com.cdwater.digitalproduct.common.exception.BusinessException;
import com.cdwater.digitalproduct.common.utils.RedisUtil;
import com.cdwater.digitalproduct.common.utils.ThreadUtil;
import com.cdwater.digitalproduct.entity.Collect;
import com.cdwater.digitalproduct.entity.Product;
import com.cdwater.digitalproduct.mapper.CollectMapper;
import com.cdwater.digitalproduct.mapper.ProductMapper;
import com.cdwater.digitalproduct.mapper.SlideshowMapper;
import com.cdwater.digitalproduct.model.dto.ProductPageDTO;
import com.cdwater.digitalproduct.model.vo.*;
import com.cdwater.digitalproduct.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;
    @Resource
    private CollectMapper collectMapper;
    @Resource
    private SlideshowMapper slideshowMapper;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public void add(Product product) {
        //设置数码产品所属店铺
        product.setShopId(ThreadUtil.getId());
        productMapper.insert(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeOne(Integer id) {
        //操作数据库
        productMapper.deleteById(id);
        collectMapper.deleteByProductId(id);
        slideshowMapper.deleteByProductId(id);

        //清理缓存，保证数据一致性
        redisUtil.delete(RedisMark.PRODUCT_DETAIL_PREFIX + id);
        redisUtil.deleteHashValue(RedisMark.SLIDESHOW_KEY, id.toString());
        redisUtil.deleteHashValue(RedisMark.PRODUCT_RECOMMEND_KEY, id.toString());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatch(List<Integer> ids) {
        //操作数据库
        productMapper.deleteByIds(ids);
        collectMapper.deleteByProductIds(ids);
        slideshowMapper.deleteByProductIds(ids);

        //清理缓存，保证数据一致性
        List<String> keys = new ArrayList<>();
        List<String> fields = new ArrayList<>();
        for (Integer id : ids) {
            keys.add(RedisMark.PRODUCT_DETAIL_PREFIX + id);
            fields.add(id.toString());
        }
        redisUtil.delete(keys);
        redisUtil.deleteHashValues(RedisMark.SLIDESHOW_KEY, fields);
        redisUtil.deleteHashValues(RedisMark.PRODUCT_RECOMMEND_KEY, fields);
    }

    @Override
    public void edit(Product product) {
        //操作数据库
        productMapper.updateById(product);

        //清理缓存，保证数据一致性
        redisUtil.delete(RedisMark.PRODUCT_DETAIL_PREFIX + product.getId());

        //如果是设置为推荐状态，清理可能存在的空标记
        if (product.getRecommend() == 1) {
            redisUtil.deleteHashValue(RedisMark.PRODUCT_RECOMMEND_KEY, RedisMark.EMPTY);
        } else {
            redisUtil.deleteHashValue(RedisMark.PRODUCT_RECOMMEND_KEY, product.getId().toString());
        }

        ProductQueryVO productQueryVO = productMapper.selectById(product.getId());
        //如果仍然是推荐状态，则重建缓存
        if (productQueryVO.getRecommend() == 1) {
            ProductShowVO productShowVO = new ProductShowVO();
            try {
                BeanUtils.copyProperties(productShowVO, productQueryVO);
            } catch (Exception e) {
                throw new BusinessException(ReturnMsg.SYSTEM_ERROR);
            }
            redisUtil.updateHashOne(RedisMark.PRODUCT_RECOMMEND_KEY, product.getId().toString(), JSON.toJSONString(productShowVO), RedisMark.PRODUCT_RECOMMEND_TTL, TimeUnit.SECONDS, false);
        }
    }

    @Override
    public ProductQueryVO query(Integer id) {
        return productMapper.selectById(id);
    }

    @Override
    public PageInfo<ProductPageVO> page(ProductPageDTO productPageDTO) {
        //是否限定店铺范围
        if (RoleType.isDigitalShop(ThreadUtil.getRole())) {
            productPageDTO.setShopId(ThreadUtil.getId());
        }
        PageHelper.startPage(productPageDTO.getPageNum(), productPageDTO.getPageSize());
        List<ProductPageVO> list = productMapper.selectPage(productPageDTO);
        return PageInfo.of(list);
    }

    @Override
    public List<ProductGroupVO> productGroup() {
        return productMapper.selectProductGroup();
    }

    @Override
    public List<ProductShowVO> getRecommend() {
        //查询redis缓存
        String key = RedisMark.PRODUCT_RECOMMEND_KEY;
        List<ProductShowVO> listCache = redisUtil.getListByHashValues(key, ProductShowVO.class);

        //命中缓存
        if (listCache != null) {
            return listCache;
        }

        //未命中缓存，查询数据库
        List<ProductShowVO> listDB = productMapper.selectRecommend12();
        if (CollectionUtils.isNotEmpty(listDB)) {
            Map<String, String> mapSave = new HashMap<>();
            listDB.forEach(item -> mapSave.put(item.getId().toString(), JSON.toJSONString(item)));
            //回写redis缓存
            redisUtil.setHash(key, mapSave, RedisMark.PRODUCT_RECOMMEND_TTL, TimeUnit.SECONDS, false);
        } else {
            //数据库无数据，缓存空标记，防止缓存穿透
            redisUtil.setHash(key, RedisMark.EMPTY_MAP, RedisMark.EMPTY_TTL, TimeUnit.SECONDS, false);
        }
        return listDB;
    }

    @Override
    public PageInfo<ProductShowVO> pagePublic(ProductPageDTO productPageDTO) {
        PageHelper.startPage(productPageDTO.getPageNum(), productPageDTO.getPageSize());
        List<ProductShowVO> list = productMapper.selectPagePublic(productPageDTO);
        return PageInfo.of(list);
    }

    @Override
    public ProductDetailVO detail(Integer id, Integer userId, Integer role) {
        //查询redis缓存
        String key = RedisMark.PRODUCT_DETAIL_PREFIX + id;
        ProductDetailVO productDetailVOCache = redisUtil.getObjByHash(key, ProductDetailVO.class);

        //命中缓存
        if (productDetailVOCache != null) {
            //命中空标记
            if (productDetailVOCache.getId() == null) {
                return new ProductDetailVO();
            }
            //命中有效值
            if (RoleType.isUser(role)) {
                //普通用户判断收藏状态
                Collect collectDB = collectMapper.selectByUserIdAndProductId(userId, id);
                productDetailVOCache.setHasCollect(collectDB != null);
            } else {
                //游客一律设置为未收藏
                productDetailVOCache.setHasCollect(false);
            }
            return productDetailVOCache;
        }

        //未命中缓存，查询数据库
        ProductDetailVO productDetailVODB = productMapper.selectDetail(id);
        if (productDetailVODB != null) {
            //回写redis缓存
            redisUtil.setHash(key, productDetailVODB, RedisMark.PRODUCT_DETAIL_TTL, TimeUnit.SECONDS, false);
            if (RoleType.isUser(role)) {
                //普通用户判断收藏状态
                Collect collectDB = collectMapper.selectByUserIdAndProductId(userId, id);
                productDetailVODB.setHasCollect(collectDB != null);
            } else {
                //游客一律设置为未收藏
                productDetailVODB.setHasCollect(false);
            }
        } else {
            //数据库无数据，缓存空对象，防止缓存穿透
            redisUtil.setHash(key, RedisMark.EMPTY_MAP, RedisMark.EMPTY_TTL, TimeUnit.SECONDS, false);
        }
        return productDetailVODB;
    }
}
