package com.cdwater.digitalproduct.service.impl;

import com.alibaba.fastjson2.JSON;
import com.cdwater.digitalproduct.common.constants.OrderStatus;
import com.cdwater.digitalproduct.common.constants.RedisMark;
import com.cdwater.digitalproduct.common.constants.RoleType;
import com.cdwater.digitalproduct.common.enums.ReturnMsg;
import com.cdwater.digitalproduct.common.exception.BusinessException;
import com.cdwater.digitalproduct.common.utils.OrderNoUtil;
import com.cdwater.digitalproduct.common.utils.RedisUtil;
import com.cdwater.digitalproduct.common.utils.ThreadUtil;
import com.cdwater.digitalproduct.entity.Address;
import com.cdwater.digitalproduct.entity.ProductOrders;
import com.cdwater.digitalproduct.entity.User;
import com.cdwater.digitalproduct.mapper.AddressMapper;
import com.cdwater.digitalproduct.mapper.ProductMapper;
import com.cdwater.digitalproduct.mapper.ProductOrdersMapper;
import com.cdwater.digitalproduct.mapper.UserMapper;
import com.cdwater.digitalproduct.model.dto.*;
import com.cdwater.digitalproduct.model.vo.*;
import com.cdwater.digitalproduct.service.ProductOrdersService;
import com.cdwater.digitalproduct.websocket.WebSocketServer;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProductOrdersServiceImpl implements ProductOrdersService {

    @Resource
    private ProductOrdersMapper productOrdersMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private AddressMapper addressMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private OrderNoUtil orderNoUtil;
    @Resource
    private WebSocketServer webSocketServer;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public PageInfo<ProductOrdersPageVO> page(ProductOrdersPageDTO productOrdersPageDTO) {
        if (RoleType.isDigitalShop(ThreadUtil.getRole())) {
            //店铺端查看本店的订单
            productOrdersPageDTO.setShopId(ThreadUtil.getId());
        }

        //管理员查看所有的订单
        PageHelper.startPage(productOrdersPageDTO.getPageNum(), productOrdersPageDTO.getPageSize());
        List<ProductOrdersPageVO> list = productOrdersMapper.selectPage(productOrdersPageDTO);
        return PageInfo.of(list);
    }

    @Override
    public void acceptOrder(Long orderNo) {
        //校验订单是否存在
        ProductOrders productOrdersDB = productOrdersMapper.selectByOrderNo(orderNo);

        //订单不存在
        if (productOrdersDB == null) {
            throw new BusinessException(ReturnMsg.NOT_FOUND);
        }

        //修改订单状态->派送中
        ProductOrders productOrders = ProductOrders.builder()
                .id(productOrdersDB.getId())
                .orderStatus(OrderStatus.DELIVERING)
                .build();
        productOrdersMapper.updateById(productOrders);
    }

    @Override
    public void deliveryOrder(Long orderNo) {
        //校验订单是否存在
        ProductOrders productOrdersDB = productOrdersMapper.selectByOrderNo(orderNo);

        //订单不存在
        if (productOrdersDB == null) {
            throw new BusinessException(ReturnMsg.NOT_FOUND);
        }

        //修改订单状态->已送达
        ProductOrders productOrders = ProductOrders.builder()
                .id(productOrdersDB.getId())
                .orderStatus(OrderStatus.DELIVERED)
                .build();
        productOrdersMapper.updateById(productOrders);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundOrder(Long orderNo) {
        //校验订单是否存在
        ProductOrders productOrdersDB = productOrdersMapper.selectByOrderNo(orderNo);

        //订单不存在
        if (productOrdersDB == null) {
            throw new BusinessException(ReturnMsg.NOT_FOUND);
        }

        //模拟退款（此处退款到用户余额，后续可扩展为原路退款）
        userMapper.addBalance(productOrdersDB.getUserId(), productOrdersDB.getProductPrice());
        //库存加一
        productMapper.addStock(productOrdersDB.getProductId());

        //清理用户余额缓存，保证数据一致性
        String balanceKey = RedisMark.USER + ":" + productOrdersDB.getUserId() + ":" + RedisMark.BALANCE;
        redisUtil.delete(balanceKey);

        //修改订单状态->已取消
        ProductOrders productOrders = ProductOrders.builder()
                .id(productOrdersDB.getId())
                .orderStatus(OrderStatus.CANCELLED)
                .build();
        productOrdersMapper.updateById(productOrders);
    }

    @Override
    public ProductOrdersPlaceVO placeOrder(ProductOrdersPlaceDTO productOrdersPlaceDTO) {
        //校验权限，保证本人操作
        if (!ThreadUtil.hasPermission(productOrdersPlaceDTO.getUserId(), RoleType.USER)) {
            throw new BusinessException(ReturnMsg.FORBIDDEN_ACCESS);
        }

        //生成订单号
        long orderNo = orderNoUtil.nextId(RedisMark.PRODUCT_ORDERS);
        //设置订单状态为待付款
        Integer orderStatus = OrderStatus.PENDING_PAYMENT;
        //设置下单时间
        LocalDateTime orderTime = LocalDateTime.now();

        //查询数码产品信息
        ProductQueryVO productQueryVO = productMapper.selectById(productOrdersPlaceDTO.getProductId());
        if (productQueryVO == null) {
            throw new BusinessException(ReturnMsg.NOT_FOUND);
        }
        String productName = productQueryVO.getName();
        String productImg = productQueryVO.getImg();
        BigDecimal productPrice = productQueryVO.getPrice();

        //查询地址信息
        Address address = addressMapper.selectById(productOrdersPlaceDTO.getAddressId());
        if (address == null) {
            throw new BusinessException(ReturnMsg.NOT_FOUND);
        }
        String consignee = address.getConsignee();
        String phoneNumber = address.getPhoneNumber();
        String provinceCode = address.getProvinceCode();
        String cityCode = address.getCityCode();
        String districtCode = address.getDistrictCode();
        String detailAddress = address.getDetailAddress();

        //封装订单信息
        ProductOrders productOrders = ProductOrders.builder()
                .orderNo(orderNo)
                .orderStatus(orderStatus)
                .orderTime(orderTime)
                .userId(productOrdersPlaceDTO.getUserId())
                .shopId(productOrdersPlaceDTO.getShopId())
                .productId(productOrdersPlaceDTO.getProductId())
                .productName(productName)
                .productImg(productImg)
                .productPrice(productPrice)
                .addressId(productOrdersPlaceDTO.getAddressId())
                .consignee(consignee)
                .phoneNumber(phoneNumber)
                .provinceCode(provinceCode)
                .cityCode(cityCode)
                .districtCode(districtCode)
                .detailAddress(detailAddress)
                .build();

        //插入数据库
        productOrdersMapper.insert(productOrders);

        //封装VO返回结果
        return ProductOrdersPlaceVO.builder()
                .orderNo(orderNo)
                .orderStatus(orderStatus)
                .orderTime(orderTime)
                .orderAmount(productPrice)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderNo) {
        //校验订单是否存在
        ProductOrders productOrdersDB = productOrdersMapper.selectByOrderNo(orderNo);

        //订单不存在
        if (productOrdersDB == null) {
            throw new BusinessException(ReturnMsg.NOT_FOUND);
        }

        //明确操作人角色
        Integer role = ThreadUtil.getRole();

        //用户操作
        if (RoleType.isUser(role)) {
            //派送中、已送达、已完成、已取消等状态下无法取消订单
            if (productOrdersDB.getOrderStatus() >= 2) {
                throw new BusinessException(ReturnMsg.SYSTEM_ERROR);
            }

            //待接单状态下取消订单需要进行退款
            if (Objects.equals(productOrdersDB.getOrderStatus(), OrderStatus.PENDING_ACCEPT)) {
                //模拟退款（此处退款到用户余额，后续可扩展为原路退款）
                userMapper.addBalance(productOrdersDB.getUserId(), productOrdersDB.getProductPrice());
                //库存加一
                productMapper.addStock(productOrdersDB.getProductId());

                //清理用户余额缓存，保证数据一致性
                String balanceKey = RedisMark.USER + ":" + productOrdersDB.getUserId() + ":" + RedisMark.BALANCE;
                redisUtil.delete(balanceKey);
            }
        } else if (RoleType.isDigitalShop(role)) {
            //店铺操作
            //已送达、已完成、已取消状态下无法取消订单
            if (productOrdersDB.getOrderStatus() >= 3) {
                throw new BusinessException(ReturnMsg.SYSTEM_ERROR);
            }

            //待接单、派送中状态下取消订单需要进行退款
            if (Objects.equals(productOrdersDB.getOrderStatus(), OrderStatus.PENDING_ACCEPT) ||
                    Objects.equals(productOrdersDB.getOrderStatus(), OrderStatus.DELIVERING)) {
                //模拟退款（此处退款到用户余额，后续可扩展为原路退款）
                userMapper.addBalance(productOrdersDB.getUserId(), productOrdersDB.getProductPrice());
                //库存加一
                productMapper.addStock(productOrdersDB.getProductId());

                //清理用户余额缓存，保证数据一致性
                String balanceKey = RedisMark.USER + ":" + productOrdersDB.getUserId() + ":" + RedisMark.BALANCE;
                redisUtil.delete(balanceKey);
            }
        } else {
            throw new BusinessException(ReturnMsg.FORBIDDEN_ACCESS);
        }

        //修改订单状态->已取消
        ProductOrders productOrders = ProductOrders.builder()
                .id(productOrdersDB.getId())
                .orderStatus(OrderStatus.CANCELLED)
                .build();
        productOrdersMapper.updateById(productOrders);
    }

    @Override
    public void completedOrder(Long orderNo) {
        //校验订单是否存在
        ProductOrders productOrdersDB = productOrdersMapper.selectByOrderNo(orderNo);

        //订单不存在
        if (productOrdersDB == null) {
            throw new BusinessException(ReturnMsg.NOT_FOUND);
        }

        //修改订单状态->已送达
        ProductOrders productOrders = ProductOrders.builder()
                .id(productOrdersDB.getId())
                .orderStatus(OrderStatus.COMPLETED)
                .build();
        productOrdersMapper.updateById(productOrders);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paymentOrder(Long orderNo) {
        //校验订单是否存在
        ProductOrders productOrdersDB = productOrdersMapper.selectByOrderNo(orderNo);

        //订单不存在
        if (productOrdersDB == null) {
            throw new BusinessException(ReturnMsg.NOT_FOUND);
        }

        User userDB = userMapper.selectById(productOrdersDB.getUserId());
        BigDecimal userBalance = userDB.getBalance();
        BigDecimal orderAmount = productOrdersDB.getProductPrice();
        //余额不足
        if (userBalance.compareTo(orderAmount) < 0) {
            throw new BusinessException(ReturnMsg.INSUFFICIENT_BALANCE);
        }

        ProductQueryVO productQueryVODB = productMapper.selectById(productOrdersDB.getProductId());
        //库存不足
        if (productQueryVODB.getStore() <= 0) {
            throw new BusinessException(ReturnMsg.STOCK_NOT_ENOUGH);
        }

        //模拟扣款（此处扣除用户余额，后续可扩展为支付）
        userMapper.subBalance(productOrdersDB.getUserId(), productOrdersDB.getProductPrice());
        //数码产品库存减一
        productMapper.subStock(productOrdersDB.getProductId());

        //清理用户余额缓存，保证数据一致性
        String balanceKey = RedisMark.USER + ":" + productOrdersDB.getUserId() + ":" + RedisMark.BALANCE;
        redisUtil.delete(balanceKey);

        //修改订单状态->待接单
        ProductOrders productOrders = ProductOrders.builder()
                .id(productOrdersDB.getId())
                .orderStatus(OrderStatus.PENDING_ACCEPT)
                .build();
        productOrdersMapper.updateById(productOrders);

        //通过WebSocket向店铺推送待接单消息
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("type", "product-order");
        messageMap.put("orderNo", orderNo.toString());
        messageMap.put("orderTime", productOrdersDB.getOrderTime());
        String messageJson = JSON.toJSONString(messageMap);
        webSocketServer.sendToClient(messageJson, productOrdersDB.getShopId().toString());
    }

    @Override
    public List<ProductOrdersShowVO> list() {
        //非用户访问
        if (!RoleType.isUser(ThreadUtil.getRole())) {
            throw new BusinessException(ReturnMsg.FORBIDDEN_ACCESS);
        }

        //获取当前用户id
        Integer userId = ThreadUtil.getId();

        return productOrdersMapper.selectByUserId(userId);
    }

    @Override
    public Integer count(Integer shopId) {
        //要求本店访问
        if (!ThreadUtil.hasPermission(shopId, RoleType.DIGITALSHOP)) {
            throw new BusinessException(ReturnMsg.FORBIDDEN_ACCESS);
        }

        //统计待接单订单数
        Integer orderStatus = OrderStatus.PENDING_ACCEPT;
        return productOrdersMapper.countByShopIdAndOrderStatus(shopId, orderStatus);
    }
}
