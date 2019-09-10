package com.jinnjo.sale.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SeckillGoodsVo {
    @ApiModelProperty(value = "商品编号")
    public String goodsId;

    @ApiModelProperty(value = "商品名称")
    public String goodsName;

    @ApiModelProperty(value = "商品规格id")
    public String goodsSpecId;

    @ApiModelProperty(value = "规格名称")
    public String goodsSpecName;

    @ApiModelProperty(value = "发行量")
    public Integer totalCount;

    @ApiModelProperty(value = "每人限领张数")
    public Integer limitCount;

    @ApiModelProperty(value = "秒杀价格")
    public BigDecimal seckillPrice;

    @ApiModelProperty(value = "活动标识")
    public String activityId;

    @ApiModelProperty(value = "虚拟销量")
    public BigDecimal virtualSales;
}
