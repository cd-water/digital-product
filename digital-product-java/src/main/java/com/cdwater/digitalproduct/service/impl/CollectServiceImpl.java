package com.cdwater.digitalproduct.service.impl;

import com.alibaba.fastjson2.JSON;
import com.cdwater.digitalproduct.common.constants.RedisMark;
import com.cdwater.digitalproduct.common.constants.RoleType;
import com.cdwater.digitalproduct.common.enums.ReturnMsg;
import com.cdwater.digitalproduct.common.exception.BusinessException;
import com.cdwater.digitalproduct.common.utils.RedisUtil;
import com.cdwater.digitalproduct.common.utils.ThreadUtil;
import com.cdwater.digitalproduct.entity.Collect;
import com.cdwater.digitalproduct.mapper.CollectMapper;
import com.cdwater.digitalproduct.mapper.ProductMapper;
import com.cdwater.digitalproduct.model.vo.CollectProductVO;
import com.cdwater.digitalproduct.model.vo.ProductQueryVO;
import com.cdwater.digitalproduct.service.CollectService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class CollectServiceImpl implements CollectService {

    @Resource
    private CollectMapper collectMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<CollectProductVO> list() {
        //查询redis缓存
        Integer userId = ThreadUtil.getId();
        String key = RedisMark.USER + ":" + userId + ":" + RedisMark.COLLECT;
        List<CollectProductVO> listCache = redisUtil.getListByHashValues(key, CollectProductVO.class);

        //命中缓存
        if (listCache != null) {
            return listCache;
        }

        //未命中缓存，数据库查询
        List<CollectProductVO> listDB = collectMapper.selectByUserId(userId);
        if (CollectionUtils.isNotEmpty(listDB)) {
            Map<String, String> mapSave = new HashMap<>();
            listDB.forEach(item -> mapSave.put(item.getProductId().toString(), JSON.toJSONString(item)));
            //回写redis缓存
            redisUtil.setHash(key, mapSave, RedisMark.COLLECT_TTL, TimeUnit.SECONDS, true);
        } else {
            //数据库无数据，缓存空标记，防止缓存穿透
            redisUtil.setHash(key, RedisMark.EMPTY_MAP, RedisMark.EMPTY_TTL, TimeUnit.SECONDS, true);
        }
        return listDB;
    }

    @Override
    public void join(Collect collect) {
        //只允许普通用户添加自己的收藏夹
        if (!ThreadUtil.hasPermission(collect.getUserId(), RoleType.USER)) {
            throw new BusinessException(ReturnMsg.FORBIDDEN_ACCESS);
        }

        //判断是否已收藏
        Collect collectDB = collectMapper.selectByUserIdAndProductId(collect.getUserId(), collect.getProductId());
        if (collectDB != null) {
            //收藏夹已存在
            throw new BusinessException(ReturnMsg.COLLECT_EXISTED);
        }

        //操作数据库
        collectMapper.insert(collect);

        //更新缓存，保证数据一致性
        Integer userId = ThreadUtil.getId();
        String key = RedisMark.USER + ":" + userId + ":" + RedisMark.COLLECT;

        //完善缓存信息
        ProductQueryVO productQueryVO = productMapper.selectById(collect.getProductId());
        CollectProductVO collectProductVO = CollectProductVO.builder()
                .id(collect.getId())
                .productId(collect.getProductId())
                .productName(productQueryVO.getName())
                .productUsed(productQueryVO.getUsed())
                .productImg(productQueryVO.getImg())
                .productPrice(productQueryVO.getPrice())
                .productStore(productQueryVO.getStore())
                .productSaleStatus(productQueryVO.getSaleStatus())
                .build();

        redisUtil.updateHashOne(key, collect.getProductId().toString(), JSON.toJSONString(collectProductVO), RedisMark.COLLECT_TTL, TimeUnit.SECONDS, true);
        redisUtil.deleteHashValue(key, RedisMark.EMPTY);
    }

    @Override
    public void out(Integer userId, Integer productId) {
        //只允许普通用户移出自己的收藏夹
        if (!ThreadUtil.hasPermission(userId, RoleType.USER)) {
            throw new BusinessException(ReturnMsg.FORBIDDEN_ACCESS);
        }

        //操作数据库
        collectMapper.deleteByUserIdAndProductId(userId, productId);

        //更新缓存，保证数据一致性
        String key = RedisMark.USER + ":" + userId + ":" + RedisMark.COLLECT;
        redisUtil.deleteHashValue(key, productId.toString());
    }
}
