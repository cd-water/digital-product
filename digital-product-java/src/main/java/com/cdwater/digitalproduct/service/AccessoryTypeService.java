package com.cdwater.digitalproduct.service;

import com.cdwater.digitalproduct.entity.AccessoryType;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AccessoryTypeService {
    void add(AccessoryType accessoryType);

    void removeOne(Integer id);

    void removeBatch(List<Integer> ids);

    void edit(AccessoryType accessoryType);

    AccessoryType query(Integer id);

    PageInfo<AccessoryType> page(AccessoryType accessoryType, Integer pageNum, Integer pageSize);

    List<AccessoryType> getAll();

    List<AccessoryType> getHot();
}
