package com.cdwater.digitalproduct.service;

import com.cdwater.digitalproduct.entity.Collect;
import com.cdwater.digitalproduct.model.vo.CollectProductVO;

import java.util.List;

public interface CollectService {

    List<CollectProductVO> list();

    void join(Collect collect);

    void out(Integer userId, Integer productId);
}
