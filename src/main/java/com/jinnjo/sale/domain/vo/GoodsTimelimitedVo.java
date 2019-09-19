package com.jinnjo.sale.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 给用户限时购限时提醒
 */
@Getter
@Setter
public class GoodsTimelimitedVo {

    private String  content;  //推送内容
    private Map extras;   //传入Json数据(秒杀 及 商品ID)
    private Integer pushtype;//定时推送(传 1)
    private String sign;      //签名
    private String startdate;//抢购时间(开抢时间减5分钟之后的时间)
    private String type;      //用户:1,商家:2
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userid;   //用户ID

}
