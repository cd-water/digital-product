package com.cdwater.digitalproduct.service;

import com.cdwater.digitalproduct.model.dto.SlideshowPageDTO;
import com.cdwater.digitalproduct.entity.Slideshow;
import com.cdwater.digitalproduct.model.vo.SlideshowPageVO;
import com.cdwater.digitalproduct.model.vo.SlideshowQueryVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SlideshowService {

    void add(Slideshow slideshow);

    void removeOne(Integer id);

    void removeBatch(List<Integer> ids);

    void edit(Slideshow slideshow);

    SlideshowQueryVO query(Integer id);

    PageInfo<SlideshowPageVO> page(SlideshowPageDTO slideshowPageDTO);

    List<Slideshow> list();
}
