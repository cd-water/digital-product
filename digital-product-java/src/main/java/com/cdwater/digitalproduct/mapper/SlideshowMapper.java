package com.cdwater.digitalproduct.mapper;

import com.cdwater.digitalproduct.model.dto.SlideshowPageDTO;
import com.cdwater.digitalproduct.entity.Slideshow;
import com.cdwater.digitalproduct.model.vo.SlideshowPageVO;
import com.cdwater.digitalproduct.model.vo.SlideshowQueryVO;

import java.util.List;

public interface SlideshowMapper {

    void insert(Slideshow slideshow);

    void updateById(Slideshow slideshow);

    SlideshowQueryVO selectById(Integer id);

    List<SlideshowPageVO> selectPage(SlideshowPageDTO slideshowPageDTO);

    List<Slideshow> selectList();

    //删除操作------------------------------------------------------------------------------------------------------------
    void deleteById(Integer id);

    void deleteByIds(List<Integer> ids);

    void deleteByProductId(Integer productId);

    void deleteByProductIds(List<Integer> productIds);

    void deleteByShopId(Integer shopId);

    void deleteByShopIds(List<Integer> shopIds);
    //删除操作------------------------------------------------------------------------------------------------------------
}




