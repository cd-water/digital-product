package com.cdwater.digitalproduct.common.constants;

import java.util.Objects;

/**
 * 角色类型
 */
public class RoleType {
    //管理员
    public static final Integer ADMIN = 0;

    //店铺
    public static final Integer DIGITALSHOP = 1;

    //普通用户
    public static final Integer USER = 2;

    public static Boolean isAdmin(Integer role) {
        return Objects.equals(role, ADMIN);
    }

    public static Boolean isDigitalShop(Integer role) {
        return Objects.equals(role, DIGITALSHOP);
    }

    public static Boolean isUser(Integer role) {
        return Objects.equals(role, USER);
    }
}
