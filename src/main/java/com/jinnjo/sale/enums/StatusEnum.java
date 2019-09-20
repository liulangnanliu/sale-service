package com.jinnjo.sale.enums;

import lombok.Getter;

/**
 * @author bobo
 * @date 2019\5\5 0005 10:17
 * description:一般状态枚举
 */
@Getter
public enum StatusEnum implements CodeEnum{
    DELETE(-1, "删除"),

    NORMAL(0, "正常");
    private Integer code;
    private String description;

    StatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
