package com.cdwater.digitalproduct.common.constants;

import java.util.*;

/**
 * redis记号
 */
public class RedisMark {
    /**
     * 验证码缓存标记
     * key格式：code:{phone}; value格式：string(验证码字符串)
     */
    public static final String CODE_PREFIX = "code:";
    public static final Integer CODE_TTL = 60;// second

    /**
     * token缓存标记
     * key格式：token:{token}; value格式：string(__white__)
     */
    public static final String TOKEN_PREFIX = "token:";
    public static final String WHITE = "__white__";

    /**
     * 用户信息缓存标记
     * key格式：profile:{token}; value格式：hash(字段->值)
     */
    public static final String PROFILE_PREFIX = "profile:";

    /**
     * 空缓存标记（缓存穿透解决策略）
     */
    public static final String EMPTY = "__empty__";
    public static final Map<String, String> EMPTY_MAP = Map.of(EMPTY, EMPTY);
    public static final Integer EMPTY_TTL = 5 * 60;// second

    /**
     * 系统公告缓存标记
     * key格式：notice; value格式：json(存对象)
     */
    public static final String NOTICE_KEY = "notice";
    public static final Integer NOTICE_TTL = 6 * 60 * 60;// second

    /**
     * 轮播图缓存标记
     * key格式：slideshow; value格式：hash({productId}->json(存对象))
     */
    public static final String SLIDESHOW_KEY = "slideshow";
    public static final Integer SLIDESHOW_TTL = 4 * 60 * 60;// second

    /**
     * 数码产品类型缓存标记
     * all key格式：product:all; value格式：json(存对象列表)
     * hot key格式：product:hot; value格式：json(存对象列表)
     */
    public static final String PRODUCT_TYPE_ALL_KEY = "productType:all";
    public static final Integer PRODUCT_TYPE_ALL_TTL = 6 * 60 * 60;// second
    public static final String PRODUCT_TYPE_HOT_KEY = "productType:hot";
    public static final Integer PRODUCT_TYPE_HOT_TTL = 60 * 60;// second


    /**
     * 数码配件类型缓存标记
     * all key格式：accessoryType:all; value格式：json(存对象列表)
     * hot key格式：accessoryType:hot; value格式：json(存对象列表)
     */
    public static final String ACCESSORY_TYPE_ALL_KEY = "accessoryType:all";
    public static final Integer ACCESSORY_TYPE_ALL_TTL = 6 * 60 * 60;// second
    public static final String ACCESSORY_TYPE_HOT_KEY = "accessoryType:hot";
    public static final Integer ACCESSORY_TYPE_HOT_TTL = 60 * 60;// second

    /**
     * 数码产品缓存标记
     * detail key格式：product:detail:{productId}; value格式：hash(字段->值)
     * recommend key格式：product:recommend; value格式：hash({productId}->json(存对象))
     */
    public static final String PRODUCT_DETAIL_PREFIX = "product:detail:";
    public static final Integer PRODUCT_DETAIL_TTL = 30 * 60;// second
    public static final String PRODUCT_RECOMMEND_KEY = "product:recommend";
    public static final Integer PRODUCT_RECOMMEND_TTL = 60 * 60;// second

    /**
     * 数码配件缓存标记
     * hot key格式：accessory:hot; value格式：hash({accessoryId}->json(存对象))
     */
    public static final String ACCESSORY_HOT_KEY = "accessory:hot";
    public static final Integer ACCESSORY_HOT_TTL = 60 * 60;// second

    /**
     * 店铺缓存标记
     * detail key格式：digitalShop:detail:{digitalShopId}; value格式：hash(字段->值)
     * hot key格式：digitalShop:hot; value格式：hash({digitalShopId}->json(存对象))
     */
    public static final String DIGITAL_SHOP_DETAIL_PREFIX = "digitalShop:detail:";
    public static final Integer DIGITAL_SHOP_DETAIL_TTL = 30 * 60;// second
    public static final String DIGITAL_SHOP_HOT_KEY = "digitalShop:hot";
    public static final Integer DIGITAL_SHOP_HOT_TTL = 60 * 60;// second

    /**
     * 用户缓存标记
     * collect key格式：user:{userId}:collect; value格式：hash({productId}->json(存对象))
     * cart key格式：user:{userId}:cart; value格式：hash({accessoryId}->json(存对象))
     * address key格式：user:{userId}:address; value格式：json(存对象列表)
     * balance key格式：user:{userId}:balance; value格式：string(余额字符串)
     */
    public static final String USER = "user";
    public static final String CART = "cart";
    public static final String COLLECT = "collect";
    public static final String ADDRESS = "address";
    public static final String BALANCE = "balance";
    public static final Integer CART_TTL = 30 * 60;
    public static final Integer COLLECT_TTL = 30 * 60;
    public static final Integer ADDRESS_TTL = 30 * 60;
    public static final Integer BALANCE_TTL = 5 * 60;

    /**
     * 订单号递增标记
     */
    public static final String PRODUCT_ORDERS = "productOrders";
    public static final String ACCESSORY_ORDERS = "accessoryOrders";
}
