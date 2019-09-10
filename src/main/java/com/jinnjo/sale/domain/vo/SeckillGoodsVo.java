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

    public SeckillGoodsVo(TimeLimitBuyVo.TimeLimitBuyGoodVo goodVo){
        this.goodsId = String.valueOf(goodVo.getGoodId());
        this.goodsName = goodVo.getGoodName();
        this.goodsSpecId = String.valueOf(goodVo.getSkuId());
        this.goodsSpecName = goodVo.getSkuName();
        this.totalCount = goodVo.getStock();
        this.limitCount = goodVo.getLimitNum();
        this.seckillPrice = goodVo.getDiscountPrice();
    }

}
