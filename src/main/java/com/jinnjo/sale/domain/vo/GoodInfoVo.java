package com.jinnjo.sale.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class GoodInfoVo {
    @ApiModelProperty(value = "秒杀开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startSeckillTime;

    @ApiModelProperty(value = "秒杀结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endSeckillTime;

    @ApiModelProperty(value = "商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;

    @ApiModelProperty(value = "商品规格id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long skuId;

    @ApiModelProperty(value = "规格名称")
    private String skuName;

    @ApiModelProperty(value = "限购数量")
    private Integer limitCount;

    @ApiModelProperty(value = "秒杀价格")
    private BigDecimal seckillPrice;

    public GoodInfoVo(){

    }

    public GoodInfoVo(DiscountSeckillInfoVo discountSeckillInfo, SeckillGoodsVo seckillGoodsVo){
        this.setStartSeckillTime(discountSeckillInfo.getStartSeckillTime());
        this.setEndSeckillTime(discountSeckillInfo.getEndSeckillTime());
        this.setGoodsId(seckillGoodsVo.getGoodsId());
        this.setSkuId(seckillGoodsVo.getGoodsSpecId());
        this.setSkuName(seckillGoodsVo.getGoodsSpecName());
        this.setLimitCount(seckillGoodsVo.getLimitCount());
        this.setSeckillPrice(seckillGoodsVo.getSeckillPrice());
    }
}
