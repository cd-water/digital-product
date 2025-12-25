package com.cdwater.digitalproduct.service;

import com.cdwater.digitalproduct.entity.Product;
import com.cdwater.digitalproduct.model.dto.ProductPageDTO;
import com.cdwater.digitalproduct.model.vo.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductService {

    void add(Product product);

    void removeOne(Integer id);

    void removeBatch(List<Integer> ids);

    void edit(Product product);

    ProductQueryVO query(Integer id);

    PageInfo<ProductPageVO> page(ProductPageDTO productPageDTO);

    List<ProductGroupVO> productGroup();

    List<ProductShowVO> getRecommend();

    ProductDetailVO detail(Integer id, Integer userId, Integer role);

    PageInfo<ProductShowVO> pagePublic(ProductPageDTO productPageDTO);
}
