package com.jinnjo.sale.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class MarketingCampaignVo {
    @ApiModelProperty(value = "名称")
    public String name;

    @ApiModelProperty(value = "营销活动开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date startTime;

    @ApiModelProperty(value = "营销活动结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date endTime;

    @ApiModelProperty(value = "使用位置 1 全部 2 App  3  小程序")
    public Integer usePosition;

    @ApiModelProperty(value = "适用商品类型( 1  所有商品  2  品类商品   3  品牌商品  4  指定商品)")
    public Integer applyGoodsType;

    @ApiModelProperty(value = "适用店铺类型 (1  电商店铺  2  楼下小店店铺   3  指定店铺)")
    public Integer applyShopType;

    @ApiModelProperty(value = "适用平台id")
    public Integer applyPlatform;

    @ApiModelProperty(value = "品类/品牌id")
    public String  categoryId;

    @ApiModelProperty(value = "优惠券类型  (1  店铺券  2  平台券)")
    public Integer discountsType;

    @ApiModelProperty(value = "电商店铺类型")
    public String shopTypeId;

    @ApiModelProperty(value = "秒杀优惠信息对象")
    public DiscountSeckillInfoVo discountSeckillInfoVo;
}
