package com.jinnjo.sale.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVo<T> {
    private List<T> content;

    private Long totalElements;

    private Long totalPages;

    private Boolean first;

    private Boolean empty;
}
