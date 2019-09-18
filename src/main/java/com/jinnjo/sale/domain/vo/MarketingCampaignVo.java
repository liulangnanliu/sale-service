package com.jinnjo.sale.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jinnjo.base.annotations.RestResource;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.core.Relation;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Relation("content")
public class MarketingCampaignVo {

    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "营销创建开始时间")
    private LocalDateTime createAt;

    @ApiModelProperty(value = "营销活动开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "营销活动结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "当前时间时间")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date currentTime = new Date();

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

    @ApiModelProperty(value = "活动通用状态1  开启  2  关闭")
    private Integer status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(value = "限时购活动状态(待开启  0  已开启 1   已结束3)")
    private Integer timeLimitStatus;

    public MarketingCampaignVo(){

    }


    public MarketingCampaignVo(String name, Date startTime, Date endTime, DiscountSeckillInfoVo discountSeckillInfo){
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.discountSeckillInfo = discountSeckillInfo;
    }
}
