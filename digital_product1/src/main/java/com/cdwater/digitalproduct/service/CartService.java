package com.cdwater.digitalproduct.service;

import com.cdwater.digitalproduct.entity.Cart;
import com.cdwater.digitalproduct.model.vo.CartAccessoryVO;

import java.util.List;

public interface CartService {
    List<CartAccessoryVO> list();

    void join(Cart cart);

    void out(Integer userId, Integer accessoryId);
}
