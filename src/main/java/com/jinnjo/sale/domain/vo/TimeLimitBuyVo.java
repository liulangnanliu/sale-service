package com.jinnjo.sale.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.List;

@Data
public class TimeLimitBuyVo {
    @ApiModelProperty(value = "限时购(yyyy-MM-dd)", name = "saleTime")
    private String saleTime;

    @ApiModelProperty(value = "开抢时间(08:00)", name = "startSeckillTime")
    private String startSeckillTime;

    @ApiModelProperty(value = "结束时间(10:00", name = "endSeckillTime")
    private String endSeckillTime;

    private List<TimeLimitBuyGoodVo> goodInfoVos;

    @Data
    public static class TimeLimitBuyGoodVo{
        @ApiModelProperty(value = "商品ID", name = "goodId")
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

        @ApiModelProperty(value = "原先购买方式（现货 0、预售 1、折扣）", name = "preBuyType")
        private Integer preBuyType;

        @ApiModelProperty(value = "原先限购数量", name = "preLimitNum")
        private Integer preLimitNum;

        @ApiModelProperty(value = "原先商品折扣价", name = "preDiscountPrice")
        private BigDecimal preDiscountPrice;

        @ApiModelProperty(value = "限购数量", name = "limitNum")
        private Integer limitNum;

        @ApiModelProperty(value = "商品限时购价", name = "discountPrice")
        private BigDecimal discountPrice;

        @ApiModelProperty(value = "已购百分比", name = "buyPercent")
        private Integer buyPercent;
    }

}
