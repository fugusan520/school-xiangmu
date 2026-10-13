package com.example.api.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Role {

    //超级管理员 不对外提供添加此权限的方法
    ROLE_SUPER_ADMIN("ROLE_SUPER_ADMIN", "超级管理员"),

//    ROLE_ADMIN("ROLE_ADMIN", "操作员相关权限"),
//
//    ROLE_COMMODITY("ROLE_COMMODITY", "商品相关权限"),
//
//    ROLE_EMPLOYEE("ROLE_EMPLOYEE", "员工相关权限"),
//
//    ROLE_SALE("ROLE_SALE", "销售相关权限"),
//
//    ROLE_WAREHOUSE("ROLE_WAREHOUSE", "仓库相关权限"),

    ROLE_USER("ROLE_USER", "普通用户权限"),

    ROLE_COURIER("ROLE_COURIER", "工作人员权限"),
    ;

    private final String value;

    private final String description;

    public static final Role[] ROLES = {
//            ROLE_ADMIN,
//            ROLE_COMMODITY,
//            ROLE_EMPLOYEE,
//            ROLE_SALE,
//            ROLE_WAREHOUSE,
            ROLE_USER,
            ROLE_COURIER,
            ROLE_SUPER_ADMIN

    };

    Role(String value, String description) {
        this.value = value;
        this.description = description;
    }

}
