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
import com.cdwater.digitalproduct.entity.AccessoryOrders;
import com.cdwater.digitalproduct.entity.User;
import com.cdwater.digitalproduct.mapper.AddressMapper;
import com.cdwater.digitalproduct.mapper.AccessoryMapper;
import com.cdwater.digitalproduct.mapper.AccessoryOrdersMapper;
import com.cdwater.digitalproduct.mapper.UserMapper;
import com.cdwater.digitalproduct.model.dto.*;
import com.cdwater.digitalproduct.model.vo.AccessoryOrdersPageVO;
import com.cdwater.digitalproduct.model.vo.AccessoryOrdersPlaceVO;
import com.cdwater.digitalproduct.model.vo.AccessoryOrdersShowVO;
import com.cdwater.digitalproduct.model.vo.AccessoryQueryVO;
import com.cdwater.digitalproduct.service.AccessoryOrdersService;
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
public class AccessoryOrdersServiceImpl implements AccessoryOrdersService {

    @Resource
    private AccessoryOrdersMapper accessoryOrdersMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private AddressMapper addressMapper;
    @Resource
    private OrderNoUtil orderNoUtil;
    @Resource
    private AccessoryMapper accessoryMapper;
    @Resource
    private WebSocketServer webSocketServer;
    @Resource
    private RedisUtil redisUtil;


    @Override
    public PageInfo<AccessoryOrdersPageVO> page(AccessoryOrdersPageDTO accessoryOrdersPageDTO) {
        if (RoleType.isDigitalShop(ThreadUtil.getRole())) {
            //店铺端查看本店的订单
            accessoryOrdersPageDTO.setShopId(ThreadUtil.getId());
        }

        //管理员查看所有的订单
        PageHelper.startPage(accessoryOrdersPageDTO.getPageNum(), accessoryOrdersPageDTO.getPageSize());
        List<AccessoryOrdersPageVO> list = accessoryOrdersMapper.selectPage(accessoryOrdersPageDTO);
        return PageInfo.of(list);
    }

    @Override
    public void acceptOrder(Long orderNo) {
        //校验订单是否存在
        AccessoryOrders accessoryOrdersDB = accessoryOrdersMapper.selectByOrderNo(orderNo);

        //订单不存在
        if (accessoryOrdersDB == null) {
            throw new BusinessException(ReturnMsg.NOT_FOUND);
        }

        //修改订单状态->派送中
        AccessoryOrders accessoryOrders = AccessoryOrders.builder()
                .id(accessoryOrdersDB.getId())
                .orderStatus(OrderStatus.DELIVERING)
                .build();
        accessoryOrdersMapper.updateById(accessoryOrders);
    }

    @Override
    public void deliveryOrder(Long orderNo) {
        //校验订单是否存在
        AccessoryOrders accessoryOrdersDB = accessoryOrdersMapper.selectByOrderNo(orderNo);

        //订单不存在
        if (accessoryOrdersDB == null) {
            throw new BusinessException(ReturnMsg.NOT_FOUND);
        }

        //修改订单状态->已送达
        AccessoryOrders accessoryOrders = AccessoryOrders.builder()
                .id(accessoryOrdersDB.getId())
                .orderStatus(OrderStatus.DELIVERED)
                .build();
        accessoryOrdersMapper.updateById(accessoryOrders);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderNo) {
        //校验订单是否存在
        AccessoryOrders accessoryOrdersDB = accessoryOrdersMapper.selectByOrderNo(orderNo);

        //订单不存在
        if (accessoryOrdersDB == null) {
            throw new BusinessException(ReturnMsg.NOT_FOUND);
        }

        //明确操作人角色
        Integer role = ThreadUtil.getRole();

        //用户操作
        if (RoleType.isUser(role)) {
            //派送中、已送达、已完成、已取消等状态下无法取消订单
            if (accessoryOrdersDB.getOrderStatus() >= 2) {
                throw new BusinessException(ReturnMsg.SYSTEM_ERROR);
            }

            //待接单状态下取消订单需要进行退款
            if (Objects.equals(accessoryOrdersDB.getOrderStatus(), OrderStatus.PENDING_ACCEPT)) {
                //模拟退款（此处退款到用户余额，后续可扩展为原路退款）
                userMapper.addBalance(accessoryOrdersDB.getUserId(), accessoryOrdersDB.getTotalPrice());
                //库存加回，销量减回
                accessoryMapper.addStockAndSubSaleVolume(accessoryOrdersDB.getAccessoryId(), accessoryOrdersDB.getQuantity());

                //清理用户余额缓存，保证数据一致性
                String balanceKey = RedisMark.USER + ":" + accessoryOrdersDB.getUserId() + ":" + RedisMark.BALANCE;
                redisUtil.delete(balanceKey);
            }
        } else if (RoleType.isDigitalShop(role)) {
            //店铺操作
            //已送达、已完成、已取消状态下无法取消订单
            if (accessoryOrdersDB.getOrderStatus() >= 3) {
                throw new BusinessException(ReturnMsg.SYSTEM_ERROR);
            }

            //待接单、派送中状态下取消订单需要进行退款
            if (Objects.equals(accessoryOrdersDB.getOrderStatus(), OrderStatus.PENDING_ACCEPT) ||
                    Objects.equals(accessoryOrdersDB.getOrderStatus(), OrderStatus.DELIVERING)) {
                //模拟退款（此处退款到用户余额，后续可扩展为原路退款）
                userMapper.addBalance(accessoryOrdersDB.getUserId(), accessoryOrdersDB.getTotalPrice());
                //库存加回，销量减回
                accessoryMapper.addStockAndSubSaleVolume(accessoryOrdersDB.getAccessoryId(), accessoryOrdersDB.getQuantity());

                //清理用户余额缓存，保证数据一致性
                String balanceKey = RedisMark.USER + ":" + accessoryOrdersDB.getUserId() + ":" + RedisMark.BALANCE;
                redisUtil.delete(balanceKey);
            }
        } else {
            throw new BusinessException(ReturnMsg.FORBIDDEN_ACCESS);
        }

        //修改订单状态->已取消
        AccessoryOrders accessoryOrders = AccessoryOrders.builder()
                .id(accessoryOrdersDB.getId())
                .orderStatus(OrderStatus.CANCELLED)
                .build();
        accessoryOrdersMapper.updateById(accessoryOrders);
    }

    @Override
    public void completedOrder(Long orderNo) {
        //校验订单是否存在
        AccessoryOrders accessoryOrdersDB = accessoryOrdersMapper.selectByOrderNo(orderNo);

        //订单不存在
        if (accessoryOrdersDB == null) {
            throw new BusinessException(ReturnMsg.NOT_FOUND);
        }

        //修改订单状态->已送达
        AccessoryOrders accessoryOrders = AccessoryOrders.builder()
                .id(accessoryOrdersDB.getId())
                .orderStatus(OrderStatus.COMPLETED)
                .build();
        accessoryOrdersMapper.updateById(accessoryOrders);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundOrder(Long orderNo) {
        //校验订单是否存在
        AccessoryOrders accessoryOrdersDB = accessoryOrdersMapper.selectByOrderNo(orderNo);

        //订单不存在
        if (accessoryOrdersDB == null) {
            throw new BusinessException(ReturnMsg.NOT_FOUND);
        }

        //模拟退款（此处退款到用户余额，后续可扩展为原路退款）
        userMapper.addBalance(accessoryOrdersDB.getUserId(), accessoryOrdersDB.getTotalPrice());
        //库存加回，销量减回
        accessoryMapper.addStockAndSubSaleVolume(accessoryOrdersDB.getAccessoryId(), accessoryOrdersDB.getQuantity());

        //清理用户余额缓存，保证数据一致性
        String balanceKey = RedisMark.USER + ":" + accessoryOrdersDB.getUserId() + ":" + RedisMark.BALANCE;
        redisUtil.delete(balanceKey);

        //修改订单状态->已取消
        AccessoryOrders accessoryOrders = AccessoryOrders.builder()
                .id(accessoryOrdersDB.getId())
                .orderStatus(OrderStatus.CANCELLED)
                .build();
        accessoryOrdersMapper.updateById(accessoryOrders);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<AccessoryOrdersPlaceVO> placeOrder(AccessoryOrdersPlaceDTO accessoryOrdersPlaceDTO) {
        //校验权限，保证本人操作
        if (!ThreadUtil.hasPermission(accessoryOrdersPlaceDTO.getUserId(), RoleType.USER)) {
            throw new BusinessException(ReturnMsg.FORBIDDEN_ACCESS);
        }

        //查询地址信息
        Address address = addressMapper.selectById(accessoryOrdersPlaceDTO.getAddressId());
        if (address == null) {
            throw new BusinessException(ReturnMsg.NOT_FOUND);
        }
        String consignee = address.getConsignee();
        String phoneNumber = address.getPhoneNumber();
        String provinceCode = address.getProvinceCode();
        String cityCode = address.getCityCode();
        String districtCode = address.getDistrictCode();
        String detailAddress = address.getDetailAddress();

        //设置订单状态为待付款
        Integer orderStatus = OrderStatus.PENDING_PAYMENT;

        //返回VO列表
        List<AccessoryOrdersPlaceVO> returnList = new ArrayList<>();

        List<AccessoryOrdersPlaceDTO.SelectAccessoryItem> selectAccessory = accessoryOrdersPlaceDTO.getSelectAccessories();
        for (AccessoryOrdersPlaceDTO.SelectAccessoryItem selectAccessoryItem : selectAccessory) {
            //生成订单号
            long orderNo = orderNoUtil.nextId(RedisMark.ACCESSORY_ORDERS);

            //设置下单时间
            LocalDateTime orderTime = LocalDateTime.now();

            //查询数码配件信息
            AccessoryQueryVO accessoryQueryVO = accessoryMapper.selectById(selectAccessoryItem.getAccessoryId());
            if (accessoryQueryVO == null) {
                throw new BusinessException(ReturnMsg.NOT_FOUND);
            }
            Integer shopId = accessoryQueryVO.getShopId();
            String accessoryName = accessoryQueryVO.getName();
            String accessoryImg = accessoryQueryVO.getImg();
            BigDecimal accessoryPrice = accessoryQueryVO.getPrice();

            //计算总价
            BigDecimal totalPrice = accessoryPrice.multiply(new BigDecimal(selectAccessoryItem.getQuantity()));

            //封装订单信息
            AccessoryOrders accessoryOrders = AccessoryOrders.builder()
                    .orderNo(orderNo)
                    .orderStatus(orderStatus)
                    .orderTime(orderTime)
                    .quantity(selectAccessoryItem.getQuantity())
                    .totalPrice(totalPrice)
                    .userId(accessoryOrdersPlaceDTO.getUserId())
                    .shopId(shopId)
                    .accessoryId(selectAccessoryItem.getAccessoryId())
                    .accessoryName(accessoryName)
                    .accessoryImg(accessoryImg)
                    .accessoryPrice(accessoryPrice)
                    .addressId(accessoryOrdersPlaceDTO.getAddressId())
                    .consignee(consignee)
                    .phoneNumber(phoneNumber)
                    .provinceCode(provinceCode)
                    .cityCode(cityCode)
                    .districtCode(districtCode)
                    .detailAddress(detailAddress)
                    .build();

            //插入数据库
            accessoryOrdersMapper.insert(accessoryOrders);

            //封装VO返回结果
            AccessoryOrdersPlaceVO accessoryOrdersPlaceVO = AccessoryOrdersPlaceVO.builder()
                    .orderNo(orderNo)
                    .orderStatus(orderStatus)
                    .orderTime(orderTime)
                    .orderAmount(totalPrice)
                    .build();

            returnList.add(accessoryOrdersPlaceVO);
        }

        return returnList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paymentOrder(List<Long> orderNoArray) {
        //下单用户
        User user = userMapper.selectById(ThreadUtil.getId());
        BigDecimal userBalance = user.getBalance();
        BigDecimal totalAmount = BigDecimal.ZERO;

        //批量发送消息
        Map<String, List<String>> allMessage = new HashMap<>();

        for (Long orderNo : orderNoArray) {
            //校验订单是否存在
            AccessoryOrders accessoryOrdersDB = accessoryOrdersMapper.selectByOrderNo(orderNo);

            //订单不存在
            if (accessoryOrdersDB == null) {
                throw new BusinessException(ReturnMsg.NOT_FOUND);
            }

            totalAmount = totalAmount.add(accessoryOrdersDB.getTotalPrice());

            AccessoryQueryVO accessoryQueryVODB = accessoryMapper.selectById(accessoryOrdersDB.getAccessoryId());
            //库存不足
            if (accessoryQueryVODB.getStore() < accessoryOrdersDB.getQuantity()) {
                throw new BusinessException(ReturnMsg.STOCK_NOT_ENOUGH);
            }

            //减库存,并添加销量
            accessoryMapper.subStockAndAddSaleVolume(accessoryOrdersDB.getAccessoryId(), accessoryOrdersDB.getQuantity());

            //修改订单状态->待接单
            AccessoryOrders accessoryOrders = AccessoryOrders.builder()
                    .id(accessoryOrdersDB.getId())
                    .orderStatus(OrderStatus.PENDING_ACCEPT)
                    .build();
            accessoryOrdersMapper.updateById(accessoryOrders);

            //通过WebSocket向店铺推送待接单消息
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("type", "accessory-order");
            messageMap.put("orderNo", orderNo.toString());
            messageMap.put("orderTime", accessoryOrdersDB.getOrderTime());
            String messageJson = JSON.toJSONString(messageMap);

            allMessage.computeIfAbsent(accessoryOrdersDB.getShopId().toString(), k -> new ArrayList<>()).add(messageJson);
        }

        //余额不足
        if (userBalance.compareTo(totalAmount) < 0) {
            throw new BusinessException(ReturnMsg.INSUFFICIENT_BALANCE);
        }

        //模拟扣款（此处扣除用户余额，后续可扩展为支付）
        userMapper.subBalance(user.getId(), totalAmount);

        //清理用户余额缓存，保证数据一致性
        String balanceKey = RedisMark.USER + ":" + user.getId() + ":" + RedisMark.BALANCE;
        redisUtil.delete(balanceKey);

        //WebSocket批量推送待接单消息
        allMessage.forEach((shopId, messageList) -> {
            for (String message : messageList) {
                webSocketServer.sendToClient(message, shopId);
            }
        });
    }

    @Override
    public List<AccessoryOrdersShowVO> list() {
        //非用户访问
        if (!RoleType.isUser(ThreadUtil.getRole())) {
            throw new BusinessException(ReturnMsg.FORBIDDEN_ACCESS);
        }

        //获取当前用户id
        Integer userId = ThreadUtil.getId();

        return accessoryOrdersMapper.selectByUserId(userId);
    }

    @Override
    public Integer count(Integer shopId) {
        //要求本店访问
        if (!ThreadUtil.hasPermission(shopId, RoleType.DIGITALSHOP)) {
            throw new BusinessException(ReturnMsg.FORBIDDEN_ACCESS);
        }

        //统计待接单订单数
        Integer orderStatus = OrderStatus.PENDING_ACCEPT;
        return accessoryOrdersMapper.countByShopIdAndOrderStatus(shopId, orderStatus);
    }
}
