package com.cdwater.digitalproduct.common.anno;

import com.cdwater.digitalproduct.common.enums.RoleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 授权注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
    /**
     * 角色类型（0-管理员，1-店铺，2-普通用户）
     */
    RoleEnum value();
}
