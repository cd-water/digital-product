package com.cdwater.digitalproduct.service;

import com.cdwater.digitalproduct.entity.Accessory;
import com.cdwater.digitalproduct.model.dto.AccessoryPageDTO;
import com.cdwater.digitalproduct.model.vo.AccessoryPageVO;
import com.cdwater.digitalproduct.model.vo.AccessoryQueryVO;
import com.cdwater.digitalproduct.model.vo.AccessoryShowVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AccessoryService {

    void add(Accessory accessory);

    void edit(Accessory accessory);

    void removeOne(Integer id);

    void removeBatch(List<Integer> ids);

    AccessoryQueryVO query(Integer id);

    PageInfo<AccessoryPageVO> page(AccessoryPageDTO accessoryPageDTO);

    List<AccessoryShowVO> getHot();

    PageInfo<AccessoryShowVO> pagePublic(AccessoryPageDTO accessoryPageDTO);
}
