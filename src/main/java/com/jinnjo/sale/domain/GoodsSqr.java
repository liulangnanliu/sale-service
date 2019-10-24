package com.jinnjo.sale.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "GoodsSqr")
@Table(name = "jz_goods_info_sqr")
public class GoodsSqr  extends BaseEntity{
    @NotNull(message = "商品名称不能为空")
    @ApiModelProperty(value = "商品名称", name = "title")
    private String title;

    @NotNull(message = "列表图片不能为空")
    @ApiModelProperty(value = "列表图片、商品列表中显示的商品图片", name = "icon")
    @Lob
    private String icon;

    @ApiModelProperty(value = "商品售卖来源(1 :自营发卡, 2 :第三方发卡, 3 :365商品,4 :龙马助农商品, 6:龙马海淘, 7:爱菊商品 )", name = "source")
    private int source;

    @ApiModelProperty(value = "商品类型(1 电子码 2电子卡密)", name = "type")
    private int type;

    @ApiModelProperty(value = "商品价格（所有规格中折扣价/预售价/限时购价/积分抵扣最低规格的原价）", name = "price")
    private BigDecimal price;

    @ApiModelProperty(value = "售价（所有规格中折扣价/预售价/限时购价/积分抵扣最低的价格）", name = "discountPrice")
    private BigDecimal discountPrice;

//    @ApiModelProperty(value = "类目id")
//    private String categoryId;

    @ApiModelProperty(value = "购买方式（现货 0、预售 1、折扣 2、限时购 3 、积分 4 ）", name = "buyType")
    private Integer buyType;

    @ApiModelProperty(value = "税费(当expensesTaxation=1时显示)", name = "expenses")
    private BigDecimal expenses;

    @ApiModelProperty(value = "税费收取(0:不收取,1:收取)", name = "expensesTaxation")
    private int expensesTaxation;

    @ApiModelProperty(value = "是否实名认证(1:是)", name = "isCertification")
    private String isCertification;

    @ApiModelProperty(value = "平台服务费", name = "platformCharge")
    private BigDecimal platformCharge;

    @ApiModelProperty(value = "365商品编号", name = "tsfGoodsId")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tsfGoodsId;

    @ApiModelProperty(value = "配送方式（配送+自提 0，仅配送 1，仅自提 2,默认按照店铺类型电商 仅配送，楼下小店 仅自提）", name = "distributionType")
    private Integer distributionType;

    @ApiModelProperty(value = "是否分享赚商品(0:否(未申请),1:是,2:报名(待审核),3:未通过)", name = "isShareGoods")
    private int isShareGoods;

    @ApiModelProperty(value = "是否积分兑换(积分兑换:1)", name = "isIntegralGoods")
    private String isIntegralGoods;

    @ApiModelProperty(value = "奖励金(当isShareGoods = 1时必填)", name = "bonus")
    private BigDecimal bonus;

    @ApiModelProperty(value = "自提佣金", name = "commission")
    private BigDecimal commission;

    @ApiModelProperty(value = "区域合伙人配送费", name = "partnerDeliveryFee")
    private BigDecimal partnerDeliveryFee = new BigDecimal(0.00);

    @ApiModelProperty(value="是否赠品(1:赠品)" , name = "isPremium")
    private String isPremium;

    @ApiModelProperty(value = "卡类型来源(0-其他 1-华润 2-油卡)", name = "cardType")
    private String cardType;

    @ApiModelProperty(value = "商品规格", name = "skuInfos")
    @NotNull(message = "商品规格信息不能为空！")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "goods_info_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private List<GoodsSkuSqr> skuInfos;

}
