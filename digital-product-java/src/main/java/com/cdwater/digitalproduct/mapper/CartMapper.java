package com.cdwater.digitalproduct.mapper;

import com.cdwater.digitalproduct.entity.Cart;
import com.cdwater.digitalproduct.model.vo.CartAccessoryVO;

import java.util.List;

public interface CartMapper {

    void insert(Cart cart);

    Cart selectByUserIdAndAccessoryId(Integer userId, Integer accessoryId);

    void updateById(Cart cart);

    List<CartAccessoryVO> selectByUserId(Integer userId);

    //删除操作-----------------------------------------------------------------------------------------------------------
    void deleteByUserIdAndAccessoryId(Integer userId, Integer accessoryId);

    void deleteByUserId(Integer userId);

    void deleteByUserIds(List<Integer> userIds);

    void deleteByAccessoryId(Integer accessoryId);

    void deleteByAccessoryIds(List<Integer> accessoryIds);
    //删除操作-----------------------------------------------------------------------------------------------------------
}




