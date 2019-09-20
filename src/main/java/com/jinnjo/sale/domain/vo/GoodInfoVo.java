package com.jinnjo.sale.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @ApiModelProperty(value = "当前时间时间")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date currentTime = new Date();

    @ApiModelProperty(value = "商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;

    @ApiModelProperty(value = "商品规格id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long skuId;

    @ApiModelProperty(value = "活动id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long marketingId;

    @ApiModelProperty(value = "规格名称")
    private String skuName;

    @ApiModelProperty(value = "限购数量")
    private Integer limitCount;

    @ApiModelProperty(value = "秒杀价格")
    private BigDecimal seckillPrice;

    public GoodInfoVo(){

    }

    public GoodInfoVo(DiscountSeckillInfoVo discountSeckillInfo, SeckillGoodsVo seckillGoodsVo,String marketingId){
        this.setStartSeckillTime(discountSeckillInfo.getStartSeckillTime());
        this.setEndSeckillTime(discountSeckillInfo.getEndSeckillTime());
        this.setGoodsId(seckillGoodsVo.getGoodsId());
        this.setSkuId(seckillGoodsVo.getGoodsSpecId());
        this.setSkuName(seckillGoodsVo.getGoodsSpecName());
        this.setLimitCount(seckillGoodsVo.getLimitCount());
        this.setSeckillPrice(seckillGoodsVo.getSeckillPrice());
        this.setMarketingId(Long.valueOf(marketingId));
    }
}
