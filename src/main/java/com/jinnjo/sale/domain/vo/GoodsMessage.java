package com.jinnjo.sale.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bobo
 * @date 2019\9\16 0016 14:30
 * description:
 */
@Data
public class GoodsMessage {
    @ApiModelProperty(value = "商品id")
    private Long goodsId;
    @ApiModelProperty(value = "1 增加标识  0 去掉标识")
    private Integer upDown;
}
