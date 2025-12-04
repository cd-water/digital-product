package com.cdwater.digitalproduct.mapper;

import com.cdwater.digitalproduct.entity.ProductType;

import java.util.List;

public interface ProductTypeMapper {

    void insert(ProductType productType);

    void deleteById(Integer id);

    void deleteByIds(List<Integer> ids);

    void updateById(ProductType productType);

    ProductType selectById(Integer id);

    List<ProductType> selectPage(ProductType productType);

    ProductType selectByName(String name);

    List<ProductType> selectAll();

    List<ProductType> selectHot4();
}




