package com.jinnjo.sale.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(value="OrderSubmitVo", description = "统一下单订单参数VO对象")
@Data
public class OrderSubmitVo {
    @NotEmpty
    @ApiModelProperty(value = "订单集合")
    private List<OrderVo> voList;

    @ApiModelProperty(value = "平台ID")
    private String platformId;

    @ApiModelProperty(value = "支付类型")
    private String payType;

    @ApiModelProperty(value = "IP")
    private String ip;

    @ApiModelProperty(value = "购物车ids")
    private Long[] cartIds;

    @ApiModelProperty(value = "地址ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long addressId;

    @ApiModelProperty(value = "经度")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    private String latitude;

    @ApiModelProperty(value = "OnLinePay-在线支付 UnderLinePay-线下支付  PayOnDelivery-货到付款")
    private String orderType;

    @ApiModelProperty(value = "省CODE")
    private String provinceCode;

    @ApiModelProperty(value = "市CODE")
    private String cityCode;

    @ApiModelProperty(value = "县/区CODE")
    private String countyCode;

    @ApiModelProperty(value = "收货人名称")
    private String addressName;

    @ApiModelProperty(value = "收货人电话")
    private String addressPhone;

    @ApiModelProperty(value = "收货人地址")
    private String address;

    @ApiModelProperty(value = "0-配送+自提，1-配送，2-自提")
    @NotNull
    private Integer sendType;

    @ApiModelProperty(value = "自提点名称")
    private String mentionName;

    @ApiModelProperty(value = "自提点Id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long mentionId;

    @ApiModelProperty(value = "自提点电话")
    private String mentionPhone;

    @ApiModelProperty(value = "自提点登录账号")
    private String mentionMoblie;
}
