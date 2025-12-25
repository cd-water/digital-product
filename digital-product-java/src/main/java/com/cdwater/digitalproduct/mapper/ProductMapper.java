package com.cdwater.digitalproduct.mapper;

import com.cdwater.digitalproduct.entity.Product;
import com.cdwater.digitalproduct.model.dto.ProductPageDTO;
import com.cdwater.digitalproduct.model.vo.*;

import java.util.List;
import java.util.Map;

public interface ProductMapper {

    void updateById(Product product);

    List<ProductPageVO> selectPage(ProductPageDTO productPageDTO);

    List<ProductGroupVO> selectProductGroup();

    Integer countBySaleStatus(Integer saleStatus);

    @SuppressWarnings("MybatisXMapperMethodInspection")
    List<Map<String, Object>> getTypeCountMap();

    void insert(Product product);

    ProductQueryVO selectById(Integer id);

    List<ProductShowVO> selectRecommend12();

    List<ProductShowVO> selectPagePublic(ProductPageDTO productPageDTO);

    ProductDetailVO selectDetail(Integer id);

    List<Integer> selectIdsByShopId(Integer shopId);

    List<Integer> selectIdsByShopIds(List<Integer> shopIds);

    void subStock(Integer id);

    void addStock(Integer id);

    //删除操作------------------------------------------------------------------------------------------------------------
    void deleteById(Integer id);

    void deleteByIds(List<Integer> ids);

    void deleteByShopId(Integer shopId);

    void deleteByShopIds(List<Integer> shopIds);
    //删除操作------------------------------------------------------------------------------------------------------------
}




