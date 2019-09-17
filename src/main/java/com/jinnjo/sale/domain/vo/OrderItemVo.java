package com.jinnjo.sale.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel(value="OrderItemVo",description = "订单子项VO对象")
@Getter
@Setter
@ToString
public class OrderItemVo {
    @NotNull
    @ApiModelProperty(value = "订单商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;

    @ApiModelProperty(value = "商品类型：1-电子码 2-电子卡密")
    private Integer type;

    @ApiModelProperty(value = "订单购买数量")
    private Integer goodsCount;

    @ApiModelProperty(value = "订单商品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "订单商品折扣价格")
    private BigDecimal discountPrice;

    @NotNull
    @ApiModelProperty(value = "订单商品规格ID")
    private String skuId;

    @ApiModelProperty(value = "类目id")
    private String categoryId;

    @ApiModelProperty(value = " 购买方式（现货 0、预售 1、折扣 2、3 限时购,4 积分）")
    private Integer buyType ;

    @ApiModelProperty(value = "分享赚  分享商铺id")
    private String shareShopId;

    @ApiModelProperty(value = "推手  分享人id")
    private String shareUserId;

    @ApiModelProperty(value = "下单立减金额")
    private BigDecimal reducePrice;

    @ApiModelProperty(value = "规格名称")
    private String spStrVal;

    @ApiModelProperty(value = "税费")
    private BigDecimal expenses;

    @ApiModelProperty(value = "税费收取(0:不收取,1:收取) ")
    private Integer expensesTaxation;

    @ApiModelProperty(value = "是否展示身份信息")
    private Integer isCertification;

    @ApiModelProperty(value = "平台服务费")
    private BigDecimal platformCharge;

    @ApiModelProperty(value = "365商品编号")
    private String tsfGoodsId;

    @ApiModelProperty(value = "配送方式（配送+自提 0，仅配送 1，仅自提 2,默认按照店铺类型电商 仅配送，楼下小店 仅自提）")
    private Integer distributionType ;

    @ApiModelProperty(value = "是否分享赚商品(0:否,1:是)")
    private int isShareGoods;

    @ApiModelProperty(value = "是否积分兑换(积分兑换:1)", name = "isIntegralGoods")
    private String isIntegralGoods;

    @ApiModelProperty(value = "奖励金(当isShareGoods = 1时必填)")
    private BigDecimal bonus;

    @ApiModelProperty(value = "自提佣金")
    private BigDecimal commission=BigDecimal.ZERO;

    @ApiModelProperty(value = "商品名称")
    private String title;

    @ApiModelProperty(value = "库存")
    private int stock;

    @ApiModelProperty(value = "列表图片、商品列表中显示的商品图片")
    private String icon;

    @ApiModelProperty(value = "商品售卖来源(1 自营发卡  2 第三方发卡 3 365商品)")
    private int source;

    @ApiModelProperty(value = "合伙人配送费")
    private BigDecimal partnerDeliveryFee = BigDecimal.ZERO;
}
