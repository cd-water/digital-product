package com.cdwater.digitalproduct.service;

import com.cdwater.digitalproduct.entity.DigitalShop;
import com.cdwater.digitalproduct.model.dto.DigitalShopPageDTO;
import com.cdwater.digitalproduct.model.vo.DigitalShopShowVO;
import com.cdwater.digitalproduct.model.vo.DigitalShopDetailVO;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface DigitalShopService {
    void add(DigitalShop digitalShop);

    void removeOne(Integer id);

    void removeBatch(List<Integer> ids);

    void edit(DigitalShop digitalShop, HttpServletRequest request);

    DigitalShop query(Integer id, HttpServletRequest request);

    PageInfo<DigitalShop> page(DigitalShopPageDTO digitalShopPageDTO);

    List<DigitalShopShowVO> getHot();

    PageInfo<DigitalShopShowVO> pagePublic(DigitalShopPageDTO digitalShopPageDTO);

    DigitalShopDetailVO detail(Integer id);
}
