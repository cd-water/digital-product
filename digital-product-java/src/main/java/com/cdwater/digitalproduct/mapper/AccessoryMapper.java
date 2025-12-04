package com.cdwater.digitalproduct.mapper;

import com.cdwater.digitalproduct.entity.Accessory;
import com.cdwater.digitalproduct.model.dto.AccessoryPageDTO;
import com.cdwater.digitalproduct.model.vo.AccessoryPageVO;
import com.cdwater.digitalproduct.model.vo.AccessoryQueryVO;
import com.cdwater.digitalproduct.model.vo.AccessoryShowVO;

import java.util.List;
import java.util.Map;

public interface AccessoryMapper {

    List<AccessoryPageVO> selectPage(AccessoryPageDTO accessoryPageDTO);

    @SuppressWarnings("MybatisXMapperMethodInspection")
    List<Map<String, Object>> getTypeCountMap();

    Integer countBySaleStatus(Integer onSale);

    void insert(Accessory accessory);

    void updateById(Accessory accessory);

    AccessoryQueryVO selectById(Integer id);

    List<AccessoryShowVO> selectHot12();

    List<AccessoryShowVO> selectPagePublic(AccessoryPageDTO accessoryPageDTO);

    List<Integer> selectIdsByShopId(Integer shopId);

    List<Integer> selectIdsByShopIds(List<Integer> shopIds);

    void subStockAndAddSaleVolume(Integer id, Integer num);

    void addStockAndSubSaleVolume(Integer id, Integer num);

    //删除操作------------------------------------------------------------------------------------------------------------
    void deleteById(Integer id);

    void deleteByIds(List<Integer> ids);

    void deleteByShopId(Integer shopId);

    void deleteByShopIds(List<Integer> shopIds);
    //删除操作------------------------------------------------------------------------------------------------------------
}




