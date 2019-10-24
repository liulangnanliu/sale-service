package com.jinnjo.sale.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jinnjo.sale.domain.GoodsSkuSqr;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Lob;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ListenGoodsView {

    @ApiModelProperty(value="商品ID" , name = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value="商品购买类型" , name = "buyType")
    private Integer buyType;

    @ApiModelProperty(value="商品名称" , name = "title")
    private String title;

    @ApiModelProperty(value="商品主图片" , name = "icon")
    private String icon;

    @ApiModelProperty(value="商铺ID" , name = "shopId")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;

    @ApiModelProperty(value="商品价格(原价<社区人>)" , name = "price")
    private BigDecimal price;

    @ApiModelProperty(value="商品价格(原价<企业购>)" , name = "costPrice")
    private BigDecimal costPrice;

    @ApiModelProperty(value=" 是否使用社区币抵扣(0-默认不使用 1-使用) ," , name = "isUsedCoin ")
    private Integer isUsedCoin ;

    @ApiModelProperty(value="商品折扣价格" , name = "discountPrice")
    private BigDecimal discountPrice;

    @ApiModelProperty(value="商品描述" , name = "outline")
    private String outline ;

    @ApiModelProperty(value="商品库存" , name = "stock")
    private Integer stock;

    @ApiModelProperty(value="商品销量" , name = "saleCount")
    private Integer saleCount;

    @ApiModelProperty(value="商品状态" , name = "status")
    private Integer status;

    @ApiModelProperty(value = "平台ID" ,name = "platfromId")
    private String platfromId;

    @ApiModelProperty(value = "商品类目ID" , name = "categoryId")
    private Long categoryId;

    @ApiModelProperty(value = "商品来源" , name = "source")
    private String source;

    @ApiModelProperty(value = "配送方式" , name = "distributionType")
    private String distributionType;

    @ApiModelProperty(value = "是否分享赚商品(是:1,否:0)" , name = "isShareGoods")
    private String isShareGoods;

    @ApiModelProperty(value = "商铺类目ID" ,name = "shopCategoryId")
    private Long shopCategoryId;

    @Lob
    @ApiModelProperty(value = "销售城市" ,name = "citycode")
    private String cityCode;

    @ApiModelProperty(value = "商品链接" , name = "links")
    private String links;

    @ApiModelProperty(value = "是否区域运营合伙人店铺商品(否:0,是:1)", name = "isRegionShopGoods")
    private Integer isRegionShopGoods;
//
//    @ApiModelProperty(value = "商品规格属性", name = "skuProperties")
//    private List<GoodsProperty> skuProperties;

    @ApiModelProperty(value = "商品规格", name = "skuInfos")
    private List<GoodsSkuSqr> skuInfos;

    @ApiModelProperty(value = "限时购标识(否:0,是:1)", name = "timeBugFlag")
    private Boolean timeBugFlag;

    @ApiModelProperty(value="是否赠品(1:赠品)" , name = "isPremium")
    private String isPremium;

    @ApiModelProperty(value = "卡类型来源(0-其他 1-华润 2-油卡)", name = "cardType")
    private String cardType;

}
