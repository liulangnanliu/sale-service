package com.jinnjo.sale.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author wanghuanping
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "jz_goods_sku_sqr")
public class GoodsSkuSqr extends BaseEntity{
    @ApiModelProperty(value = "规格图片", name = "icon")
    @Lob
    private String icon;

    @NotNull(message = "商品价格不能为空")
    @ApiModelProperty(value = "商品价格", name = "price")
    private BigDecimal price;

    //商品微服务系统提供极简活动类型，针对售卖类型为折扣价/预售价/限时购价/积分抵扣的商品在商品规格中提供一个折扣价/预售价/限时购价/积分抵扣 现货商品忽略该字段，
    //另外只有现货商品可以参与微服务活动中 折扣价/预售价/限时购价/积分抵扣不参加微服务活动中的活动
    @ApiModelProperty(value = "折扣价/预售价/限时购价/积分抵扣", name = "discountPrice")
    private BigDecimal discountPrice; //折扣价/预售价/限时购价/积分抵扣

    @ApiModelProperty(value = "成本价", name = "costPrice")
    private BigDecimal costPrice;

    @NotNull(message = "库存不能为空")
    @ApiModelProperty(value = "库存", name = "stock")
    @Column(updatable = false,name = "stock")
    private int stock; //库存

    @ApiModelProperty(value = "商品规格码", name = "code")
    private String code;

    @ApiModelProperty(value = "自提佣金(已提到列表,暂不用)", name = "charges")
    private BigDecimal charges; //自提佣金

    @NotNull(message = "规格属性名和属性值的组合串不能为空")
    @ApiModelProperty(value = "规格属性名和属性值的组合串(添加商品的格式为款式|A款|颜色|红色 ,查询返回格式去除规格属性名格式为:A款|红色 ,供购物车返回已经选择的商品和订单详情显示实际购买的规格属性名)", name = "spStr")
    private String spStr;

    @ApiModelProperty(value = "供购物车返回已经选择的商品和订单详情显示实际购买的规格属性名", name = "spStrVal")
    @Transient
    private String spStrVal;

    @ApiModelProperty(value = "规格属性名ID和属性值ID的组合串(例如：1|1|2|1)", name = "spidStr")
    private String spidStr;

}
