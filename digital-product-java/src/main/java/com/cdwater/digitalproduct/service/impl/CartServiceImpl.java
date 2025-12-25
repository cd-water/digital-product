package com.cdwater.digitalproduct.service.impl;

import com.alibaba.fastjson2.JSON;
import com.cdwater.digitalproduct.common.constants.RedisMark;
import com.cdwater.digitalproduct.common.constants.RoleType;
import com.cdwater.digitalproduct.common.constants.TextInfo;
import com.cdwater.digitalproduct.common.enums.ReturnMsg;
import com.cdwater.digitalproduct.common.exception.BusinessException;
import com.cdwater.digitalproduct.common.utils.RedisUtil;
import com.cdwater.digitalproduct.common.utils.ThreadUtil;
import com.cdwater.digitalproduct.entity.Cart;
import com.cdwater.digitalproduct.mapper.CartMapper;
import com.cdwater.digitalproduct.mapper.AccessoryMapper;
import com.cdwater.digitalproduct.model.vo.CartAccessoryVO;
import com.cdwater.digitalproduct.model.vo.AccessoryQueryVO;
import com.cdwater.digitalproduct.service.CartService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CartServiceImpl implements CartService {

    @Resource
    private CartMapper cartMapper;
    @Resource
    private AccessoryMapper accessoryMapper;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<CartAccessoryVO> list() {
        //查询redis缓存
        Integer userId = ThreadUtil.getId();
        String key = RedisMark.USER + ":" + userId + ":" + RedisMark.CART;
        List<CartAccessoryVO> listCache = redisUtil.getListByHashValues(key, CartAccessoryVO.class);

        //命中缓存
        if (listCache != null) {
            return listCache;
        }

        //未命中缓存，数据库查询
        List<CartAccessoryVO> listDB = cartMapper.selectByUserId(userId);
        if (CollectionUtils.isNotEmpty(listDB)) {
            Map<String, String> mapSave = new HashMap<>();
            listDB.forEach(item -> mapSave.put(item.getAccessoryId().toString(), JSON.toJSONString(item)));
            //回写redis缓存
            redisUtil.setHash(key, mapSave, RedisMark.CART_TTL, TimeUnit.SECONDS, true);
        } else {
            //数据库无数据，缓存空标记，防止缓存穿透
            redisUtil.setHash(key, RedisMark.EMPTY_MAP, RedisMark.EMPTY_TTL, TimeUnit.SECONDS, true);
        }
        return listDB;
    }

    @Override
    public void join(Cart cart) {
        //只允许普通用户添加自己的购物车
        if (!ThreadUtil.hasPermission(cart.getUserId(), RoleType.USER)) {
            throw new BusinessException(ReturnMsg.FORBIDDEN_ACCESS);
        }

        //判断是否是第一次添加购物车
        Cart cartDB = cartMapper.selectByUserIdAndAccessoryId(cart.getUserId(), cart.getAccessoryId());
        if (cartDB == null) {
            //初次添加购物车，初始默认购买数量为1
            cart.setQuantity(TextInfo.INIT_CART_QUANTITY);
            cartMapper.insert(cart);
        } else {
            //购物车已存在，则数量加1
            cart.setId(cartDB.getId());
            cart.setQuantity(cartDB.getQuantity() + TextInfo.INIT_CART_QUANTITY);
            cartMapper.updateById(cart);
        }

        //更新缓存，保证数据一致性
        Integer userId = ThreadUtil.getId();
        String key = RedisMark.USER + ":" + userId + ":" + RedisMark.CART;

        //完善缓存信息
        AccessoryQueryVO accessoryQueryVO = accessoryMapper.selectById(cart.getAccessoryId());
        CartAccessoryVO cartAccessoryVO = CartAccessoryVO.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .quantity(cart.getQuantity())
                .accessoryId(cart.getAccessoryId())
                .shopId(accessoryQueryVO.getShopId())
                .accessoryName(accessoryQueryVO.getName())
                .accessoryImg(accessoryQueryVO.getImg())
                .accessoryPrice(accessoryQueryVO.getPrice())
                .accessoryStore(accessoryQueryVO.getStore())
                .accessorySaleStatus(accessoryQueryVO.getSaleStatus())
                .build();

        redisUtil.updateHashOne(key, cart.getAccessoryId().toString(), JSON.toJSONString(cartAccessoryVO), RedisMark.CART_TTL, TimeUnit.SECONDS, true);
        redisUtil.deleteHashValue(key, RedisMark.EMPTY);
    }

    @Override
    public void out(Integer userId, Integer accessoryId) {
        //只允许普通用户移出自己的购物车
        if (!ThreadUtil.hasPermission(userId, RoleType.USER)) {
            throw new BusinessException(ReturnMsg.FORBIDDEN_ACCESS);
        }

        //操作数据库
        cartMapper.deleteByUserIdAndAccessoryId(userId, accessoryId);

        //更新缓存，保证数据一致性
        String key = RedisMark.USER + ":" + userId + ":" + RedisMark.CART;
        redisUtil.deleteHashValue(key, accessoryId.toString());
    }
}
