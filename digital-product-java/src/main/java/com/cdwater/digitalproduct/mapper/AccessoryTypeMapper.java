package com.cdwater.digitalproduct.mapper;

import com.cdwater.digitalproduct.entity.AccessoryType;

import java.util.List;

public interface AccessoryTypeMapper {

    void insert(AccessoryType accessoryType);

    void deleteById(Integer id);

    void deleteByIds(List<Integer> ids);

    void updateById(AccessoryType accessoryType);

    AccessoryType selectById(Integer id);

    List<AccessoryType> selectPage(AccessoryType accessoryType);

    AccessoryType selectByName(String name);

    List<AccessoryType> selectAll();

    List<AccessoryType> selectHot4();
}




