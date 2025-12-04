package com.cdwater.digitalproduct.mapper;

import com.cdwater.digitalproduct.entity.Collect;
import com.cdwater.digitalproduct.model.vo.CollectProductVO;

import java.util.List;

public interface CollectMapper {

    void insert(Collect collect);

    Collect selectByUserIdAndProductId(Integer userId, Integer productId);

    List<CollectProductVO> selectByUserId(Integer userId);

    //删除操作------------------------------------------------------------------------------------------------------------
    void deleteByUserIdAndProductId(Integer userId, Integer productId);

    void deleteByUserId(Integer userId);

    void deleteByUserIds(List<Integer> userIds);

    void deleteByProductId(Integer productId);

    void deleteByProductIds(List<Integer> productIds);
    //删除操作------------------------------------------------------------------------------------------------------------
}




