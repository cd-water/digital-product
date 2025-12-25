package com.cdwater.digitalproduct.mapper;

import com.cdwater.digitalproduct.entity.DigitalShop;
import com.cdwater.digitalproduct.model.dto.DigitalShopPageDTO;
import com.cdwater.digitalproduct.model.vo.DigitalShopShowVO;
import com.cdwater.digitalproduct.model.vo.DigitalShopDetailVO;

import java.util.List;

public interface DigitalShopMapper {

    void insert(DigitalShop digitalShop);

    void deleteById(Integer id);

    void deleteByIds(List<Integer> ids);

    void updateById(DigitalShop digitalShop);

    DigitalShop selectById(Integer id);

    List<DigitalShop> selectPage(DigitalShopPageDTO digitalShopPageDTO);

    DigitalShop selectByUsername(String username);

    DigitalShop selectByPhone(String phone);

    Integer countByAuditStatus(Integer auditStatus);

    List<DigitalShopShowVO> selectHot6();

    List<DigitalShopShowVO> selectPagePublic(DigitalShopPageDTO digitalShopPageDTO);

    DigitalShopDetailVO selectDetail(Integer id);
}




