package com.jinnjo.sale.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class DiscountSeckillInfoVo {
    @ApiModelProperty(value = "优惠类型( 1  秒杀)")
    public Integer discountsType = 1;

    @ApiModelProperty(value = "是否与其他优惠券同时使用,默认否")
    public Boolean isSwitch = false;

    @ApiModelProperty(value = "秒杀开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date startSeckillTime;

    @ApiModelProperty(value = "秒杀结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date endSeckillTime;

    @ApiModelProperty(value = "秒杀商品列表")
    public List<SeckillGoodsVo> seckillGoodsList;

    public DiscountSeckillInfoVo(){

    }

    public DiscountSeckillInfoVo(Date startSeckillTime, Date endSeckillTime, List<SeckillGoodsVo> seckillGoodsList){
        this.startSeckillTime = startSeckillTime;
        this.endSeckillTime = endSeckillTime;
        this.seckillGoodsList = seckillGoodsList;
    }

}
