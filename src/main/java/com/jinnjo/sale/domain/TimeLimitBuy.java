package com.jinnjo.sale.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jinnjo.sale.domain.vo.TimeLimitBuyVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "jz_time_limit_buy")
@ApiModel(value="timeLimitBuy",description="限时购商品活动(status:1 启用,0 不启用)")
public class TimeLimitBuy extends BaseEntity{
    @ApiModelProperty(value = "商品ID", name = "goodId")
    @Column(name = "good_id",columnDefinition="bigint(20) NOT NULL COMMENT '商品ID'")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodId;

    @ApiModelProperty(value = "商品名称", name = "goodName")
    @Column(name = "good_name",columnDefinition="varchar(255) NOT NULL COMMENT '商品名称'")
    private String goodName;

    @ApiModelProperty(value = "商品图片", name = "goodImg")
    @Column(name = "good_img",columnDefinition="varchar(1024) NOT NULL COMMENT '商品图片'")
    private String goodImg;

    @ApiModelProperty(value="规格ID",name="skuId")
    @Column(name = "sku_id",columnDefinition="bigint(20) NOT NULL COMMENT '商品规格id'")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long skuId;

    @ApiModelProperty(value="规格名称",name="skuName")
    @Column(name = "sku_name",columnDefinition="varchar(255) NOT NULL COMMENT '商品规格名称'")
    private String skuName;

    @ApiModelProperty(value = "商品原先购买方式（现货 0、预售 1、折扣）", name = "buyType")
    @Column(name = "buy_type",columnDefinition="int NOT NULL COMMENT '商品原先购买方式'")
    private Integer buyType;

    @ApiModelProperty(value = "商品原先折扣价", name = "discountPrice")
    @Column(name = "discount_price",columnDefinition="decimal(19) DEFAULT NULL COMMENT '商品原先折扣价'")
    private BigDecimal discountPrice;

    @ApiModelProperty(value = "商品原先限购数量", name = "limitNum")
    @Column(name = "limit_num",columnDefinition="int DEFAULT NULL COMMENT '商品原先限购数量'")
    private Integer limitNum;

    @ApiModelProperty(value = "发布数量(等于当前库存)", name = "releaseNum")
    @Column(name = "release_num",columnDefinition="int NOT NULL COMMENT '发布数量'")
    private Integer releaseNum;

    @ApiModelProperty(value = "抢购数量", name = "buyNum")
    @Column(name = "buy_num",columnDefinition="int DEFAULT NULL COMMENT '抢购数量'")
    private Integer buyNum;

    @ApiModelProperty(value = "已购百分比", name = "buyPercent")
    @Column(name = "buy_percent",columnDefinition="int DEFAULT NULL COMMENT '已购百分比'")
    private Integer buyPercent;

    @ApiModelProperty(value = "营销活动ID", name = "campaignId")
    @Column(name = "campaign_id",columnDefinition="bigint(20) NOT NULL COMMENT '营销活动ID'")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long campaignId;

    @Column(name = "sale_time",columnDefinition = "date NOT NULL COMMENT '活动日期'")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date saleTime;

    public TimeLimitBuy(){

    }
    public TimeLimitBuy(Date saleTime,String campaignId, TimeLimitBuyVo.TimeLimitBuyGoodVo timeLimitBuyGoodVo){
        this.saleTime = saleTime;
        this.campaignId = Long.parseLong(campaignId);
        this.goodId = timeLimitBuyGoodVo.getGoodId();
        this.goodName = timeLimitBuyGoodVo.getGoodName();
        this.goodImg = timeLimitBuyGoodVo.getGoodImg();
        this.buyType = timeLimitBuyGoodVo.getPreBuyType();
        this.skuId = timeLimitBuyGoodVo.getSkuId();
        this.skuName = timeLimitBuyGoodVo.getSkuName();
        this.discountPrice = timeLimitBuyGoodVo.getPreDiscountPrice();
        this.releaseNum = timeLimitBuyGoodVo.getStock();
        this.limitNum = timeLimitBuyGoodVo.getPreLimitNum();
        this.buyPercent = timeLimitBuyGoodVo.getBuyPercent();

    }
}
