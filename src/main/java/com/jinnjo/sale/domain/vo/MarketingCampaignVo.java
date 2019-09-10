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

    @ApiModelProperty(value = "适用平台id")
    public Integer applyPlatform;

    @ApiModelProperty(value = "秒杀优惠信息对象")
    public DiscountSeckillInfoVo discountSeckillInfoVo;
}
