package com.cdwater.digitalproduct.service;

import com.cdwater.digitalproduct.entity.ProductType;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductTypeService {
    void add(ProductType productType);

    void removeOne(Integer id);

    void removeBatch(List<Integer> ids);

    void edit(ProductType productType);

    ProductType query(Integer id);

    PageInfo<ProductType> page(ProductType productType, Integer pageNum, Integer pageSize);

    List<ProductType> getAll();

    List<ProductType> getHot();
}
