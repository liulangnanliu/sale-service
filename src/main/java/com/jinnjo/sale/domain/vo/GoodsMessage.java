package com.jinnjo.sale.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty(value = "限时购标识(否:0,是:1)")
    private Boolean timeBugFlag;

    public GoodsMessage(){

    }

    public GoodsMessage(Long goodsId, Boolean timeBugFlag){
        this.goodsId = goodsId;
        this.timeBugFlag = timeBugFlag;
    }
}
