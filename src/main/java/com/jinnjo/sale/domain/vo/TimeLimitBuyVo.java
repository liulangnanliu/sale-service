package com.jinnjo.sale.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class TimeLimitBuyVo {
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date saleTime;

    private String startSeckillTime;

    private String endSeckillTime;

    @ApiModelProperty(value = "商品ID", name = "goodId")
    @Column(name = "good_id",columnDefinition="bigint(20) NOT NULL COMMENT '商品ID'")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodId;

    @ApiModelProperty(value = "商品名称", name = "goodName")
    @Column(name = "good_name",columnDefinition="varchar(255) DEFAULT NULL COMMENT '商品名称'")
    private String goodName;

    @ApiModelProperty(value = "商品图片", name = "goodImg")
    private String goodImg;

    @ApiModelProperty(value="规格ID",name="skuId")
    private Long skuId;

    @ApiModelProperty(value="规格名称",name="skuName")
    private String skuName;

    @ApiModelProperty(value="库存",name="stock")
    private Integer stock;

    @ApiModelProperty(value = "商品折扣价", name = "discountPrice")
    private BigDecimal discountPrice;

    @ApiModelProperty(value = "限购数量", name = "timeLimitNum")
    private Integer timeLimitNum;

    @ApiModelProperty(value = "已购百分比", name = "buyPercent")
    private Integer buyPercent;
}
