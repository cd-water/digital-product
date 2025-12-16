package com.cdwater.digitalproduct.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色枚举
 */
@AllArgsConstructor
@Getter
public enum RoleEnum {
    ADMIN(0),
    DIGITALSHOP(1),
    USER(2);

    private final Integer value;
}
