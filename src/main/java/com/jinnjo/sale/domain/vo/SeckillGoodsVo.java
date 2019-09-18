package com.jinnjo.sale.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SeckillGoodsVo {
    @ApiModelProperty(value = "商品编号")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品图片")
    private String goodsImg;

    @ApiModelProperty(value = "商品规格id")
    private Long goodsSpecId;

    @ApiModelProperty(value = "规格名称")
    private String goodsSpecName;

    @ApiModelProperty(value = "发行量")
    private Integer totalCount;

    @ApiModelProperty(value = "每人限领张数")
    private Integer limitCount;

    @ApiModelProperty(value = "秒杀价格")
    private BigDecimal seckillPrice;

    @ApiModelProperty(value = "商品的状态 0 正常 4 删除|下架")
    private Integer status;

    @ApiModelProperty(value = "销售城市")
    private String citycode;

    @ApiModelProperty(value = "原价")
    private BigDecimal price;

    @ApiModelProperty(value = "折扣价")
    private BigDecimal discountPrice;
}
