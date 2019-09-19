package com.jinnjo.sale.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 订单VO对象
 */
@ApiModel(value="OrderVo",description = "订单VO对象")
@Getter
@Setter
@ToString
public class OrderVo {

    @ApiModelProperty(value = "商家ID")
    private String shopId;

    @ApiModelProperty(value = "OnLinePay-在线支付 UnderLinePay-线下支付  PayOnDelivery-货到付款")
    private String orderType;

    @ApiModelProperty(value = "订单来源:ios,android,PC,applet")
    private String orderSource;

    @ApiModelProperty(value = "用户备注")
    private String userInfo;

    @ApiModelProperty(value = "身份ID")
    private String identityId;

    @ApiModelProperty(value = "身份名称")
    private String identityName;

    @ApiModelProperty(value = "订单下单渠道 1直播购买 2限时购下单 ")
    private Integer orderChannel = 2;

    @NotEmpty
    @ApiModelProperty(value = "订单子项")
    public List<OrderItemVo> orderItemVOList;
}
