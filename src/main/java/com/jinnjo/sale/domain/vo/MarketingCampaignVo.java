package com.jinnjo.sale.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class MarketingCampaignVo {

    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "营销活动开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "营销活动结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "使用位置 1 全部 2 App  3  小程序")
    private Integer usePosition = 1;

    @ApiModelProperty(value = "适用平台id")
    private Integer applyPlatform = 101;

    @NotNull
    @ApiModelProperty(value = "秒杀优惠信息对象")
    private DiscountSeckillInfoVo discountSeckillInfo;

    @ApiModelProperty(value = "距离开始时间")
    private Long interval;

    @ApiModelProperty(value = "优惠券类型 (1 店铺券 2 平台券)")
    private Integer discountsType;

    @ApiModelProperty(value = "活动状态")
    private Integer status;

    public MarketingCampaignVo(){

    }


    public MarketingCampaignVo(String name, Date startTime, Date endTime, DiscountSeckillInfoVo discountSeckillInfo){
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.discountSeckillInfo = discountSeckillInfo;
    }
}
