package com.cdwater.digitalproduct.task;

import com.cdwater.digitalproduct.common.constants.OrderStatus;
import com.cdwater.digitalproduct.entity.AccessoryOrders;
import com.cdwater.digitalproduct.entity.ProductOrders;
import com.cdwater.digitalproduct.mapper.AccessoryOrdersMapper;
import com.cdwater.digitalproduct.mapper.ProductOrdersMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Resource
    private ProductOrdersMapper productOrdersMapper;
    @Resource
    private AccessoryOrdersMapper accessoryOrdersMapper;

    /**
     * 订单超时取消任务
     */
    @Scheduled(cron = "0 */5 * * * *")
    public void processOrderCancelled() {
        log.info("定时处理待付款超时订单任务：{}", LocalDateTime.now());

        //处理超时10分钟的待付款订单
        LocalDateTime time = LocalDateTime.now().minusMinutes(10);
        //查询超时数码产品订单和数码配件订单
        List<ProductOrders> productOrdersList = productOrdersMapper.selectByOrderStatusAndBeforeTime(OrderStatus.PENDING_PAYMENT, time);
        List<AccessoryOrders> accessoryOrdersList = accessoryOrdersMapper.selectByOrderStatusAndBeforeTime(OrderStatus.PENDING_PAYMENT, time);

        //修改订单状态->已取消
        if (CollectionUtils.isNotEmpty(productOrdersList)) {
            productOrdersList.forEach(item -> {
                ProductOrders productOrders = ProductOrders.builder()
                        .id(item.getId())
                        .orderStatus(OrderStatus.CANCELLED)
                        .build();
                productOrdersMapper.updateById(productOrders);
            });
        }
        if (CollectionUtils.isNotEmpty(accessoryOrdersList)) {
            accessoryOrdersList.forEach(item -> {
                AccessoryOrders accessoryOrders = AccessoryOrders.builder()
                        .id(item.getId())
                        .orderStatus(OrderStatus.CANCELLED)
                        .build();
                accessoryOrdersMapper.updateById(accessoryOrders);
            });
        }
    }

    /**
     * 订单自动确认完成
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processOrderCompleted() {
        log.info("定时处理自动确定完成订单任务：{}", LocalDateTime.now());

        //处理昨天未点已完成的已送达订单
        LocalDateTime time = LocalDateTime.now().minusMinutes(60);

        //查询为点已完成的数码产品订单和数码配件订单
        List<ProductOrders> productOrdersList = productOrdersMapper.selectByOrderStatusAndBeforeTime(OrderStatus.DELIVERED, time);
        List<AccessoryOrders> accessoryOrdersList = accessoryOrdersMapper.selectByOrderStatusAndBeforeTime(OrderStatus.DELIVERED, time);

        //修改订单状态->已完成
        if (CollectionUtils.isNotEmpty(productOrdersList)) {
            productOrdersList.forEach(item -> {
                ProductOrders productOrders = ProductOrders.builder()
                        .id(item.getId())
                        .orderStatus(OrderStatus.COMPLETED)
                        .build();
                productOrdersMapper.updateById(productOrders);
            });
        }
        if (CollectionUtils.isNotEmpty(accessoryOrdersList)) {
            accessoryOrdersList.forEach(item -> {
                AccessoryOrders accessoryOrders = AccessoryOrders.builder()
                        .id(item.getId())
                        .orderStatus(OrderStatus.COMPLETED)
                        .build();
                accessoryOrdersMapper.updateById(accessoryOrders);
            });
        }
    }
}
