package com.jinnjo.sale.domain.vo;

import com.jinnjo.sale.utils.SignatureUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: LiangWei.
 * Date     : 2018/3/26.
 * Description : 发送订单延时处理
 */
@Data
@ApiModel(value = "订单信息", description = "订单传递参数")
public class OrderMsgText {
    private static final long serialVersionUID = 1L;
    /**
     * 1 社区人订单 2 企业购订单  3 生鲜柜订单
     */
    @ApiModelProperty(value = "订单类型",name = "type")
    public  int  type;

    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    @ApiModelProperty(value = "订单id",name = "orderId")
    public String  orderId;
    /**
     * 延时处理时长 单位毫秒  //最大24小时，超过24小时按照24小时算
     */
    @ApiModelProperty(value = "延时处理时长 单位毫秒",name = "delayTime")
    public long  delayTime;
    @NotBlank
    @ApiModelProperty(value = "回调url",name = "notifyUrl")
    /**
     * 处理url
     */
    public String notifyUrl;
    @ApiModelProperty(value = "签名",name = "sign")
    public String sign;

    public OrderMsgText(long delayTime, String notifyUrl){
        this.delayTime = delayTime;
        this.notifyUrl = notifyUrl;
        this.sign = SignatureUtil.signParams(this);
    }

    public OrderMsgText(){

    }
}
