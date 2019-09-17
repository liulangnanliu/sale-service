package com.jinnjo.sale.domain.vo;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jinnjo.base.annotations.RestResource;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderUnifiedVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "主键", name = "id")
    private Long id;

    @ApiModelProperty(value = "下单用户")
    private String userId;

    @ApiModelProperty(value = "下单用户资源")
    private String userLink;

    @ApiModelProperty(value = "平台ID")
    private String platformId;

    @ApiModelProperty(value = "平台资源链接")
    private String platformLink;

    @ApiModelProperty(value = "大订单号")
    private String bigOrder;

    @ApiModelProperty(value = "365父订单号")
    private String OrderNo;

    @ApiModelProperty(value = "订单集合")
    @RestResource(rel = "orders")
    public List<OrderInfo> orderInfoList;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createDate;

    @ApiModelProperty(value = "支付金额")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "积分")
    private BigDecimal integral;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "优惠卷ID")
    private Long couponId;

    @ApiModelProperty(value = "是否使用社区币抵扣 0不使用 1使用  默认为0")
    private int sqbMark;

    @ApiModelProperty(value = "社区币抵扣金额")
    private BigDecimal currency = BigDecimal.ZERO;

    @ApiModelProperty(value = "优惠券金额")
    private BigDecimal couponPrice = BigDecimal.ZERO;


    @Data
    public class OrderInfo {
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @ApiModelProperty(value = "主键", name = "id")
        private Long id;

        @ApiModelProperty(value = "Alipay-支付宝: MicroMessenger-微信 CardBagPay-卡包支付 integralPay-积分支付  balancePay-商家余额支付 cashPayment-现金支付,nonPayment-未收款,notHave-无(全部抵扣完),transferPay-对公转账")
        private String payType;

        @ApiModelProperty(value = "支付方式描述")
        private String payTypeDsc;

        @ApiModelProperty(value = "OnLinePay-在线支付 UnderLinePay-线下支付  PayOnDelivery-货到付款")
        private String orderType;

        @ApiModelProperty(value = "平台ID")
        private String platformId;

        @ApiModelProperty(value = "平台资源链接")
        private String platformLink;

        @ApiModelProperty(value = "订单号")
        private String orderId;

        @ApiModelProperty(value = "大订单号")
        private String bigOrder;

        @ApiModelProperty(value = "365包裹号")
        private String packageNo;

        @ApiModelProperty(value = "税费")
        private BigDecimal taxFee = BigDecimal.ZERO;

        @ApiModelProperty(value = "订单状态:0-00待支付 0-01已支付,待接单/4-01货到付款未支付,待接单 0-02已接单,待发货 0-03已提醒商家发货 0-04已发货,待收货 2-01交易成功(已收货,待评价) 2-02交易成功(已评价) 1-01订单关闭(用户取消) 1-02订单关闭(商户取消) 1-03订单关闭(系统自动取消) 1-04订单关闭(商家拒绝订单) ")
        private String status;

        @ApiModelProperty(value = "订单状态描述")
        @Transient
        private String statusDsc;

        @ApiModelProperty(value = "下单用户ID")
        private String userId;

        @ApiModelProperty(value = "下单用户资源")
        private String userLink;

        @ApiModelProperty(value = "下单用户名")
        private String userName;

        @ApiModelProperty(value = "下单用户头像")
        private String userIcon;

        @ApiModelProperty(value = "商家ID")
        private String shopId;

        @ApiModelProperty(value = "店铺类型")
        private String shopTypeCode;

        @ApiModelProperty(value = "商家资源")
        private String shopLink;

        @ApiModelProperty(value = "订单总价")
        private BigDecimal TotalPrice;

        @ApiModelProperty(value = "订单实付金额")
        private BigDecimal amountsActually = BigDecimal.valueOf(0.00);

        @ApiModelProperty(value = "实际应付金额")
        private BigDecimal realPrice = BigDecimal.valueOf(0.00);

        @ApiModelProperty(value = "实际商品折扣总金额")
        private BigDecimal totalRealPrice = BigDecimal.valueOf(0.00);

        @ApiModelProperty(value = "添加时间")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date createDate;

        @ApiModelProperty(value = "支付时间")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date payDate;

        @ApiModelProperty(value = "地址ID")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long addressId;

        @ApiModelProperty(value = "市CODE")
        private String cityCode;

        @ApiModelProperty(value = "收货人名称")
        private String addressName;

        @ApiModelProperty(value = "收货人电话")
        private String addressPhone;

        @ApiModelProperty(value = "收货人地址")
        private String address;

        @ApiModelProperty(value = "商家删除：0-未删除 1-已删除")
        private int isShopDel = 0;

        @ApiModelProperty(value = "用户删除：0-未删除 1-已删除")
        private int isDel = 0;

        @ApiModelProperty(value = "订单来源:ios,android,PC,applet")
        private String orderSource;

        @ApiModelProperty(value = "订单购买来源:SQR_USER,SQR_BUSINESS,ENT_USER")
        private String buySource;

        @ApiModelProperty(value = "用户备注")
        private String userInfo;

        @ApiModelProperty(value = "商家备注")
        private String shopInfo;

        @ApiModelProperty(value = "支付交易号")
        private String paymentCode;

        @ApiModelProperty(value = "资金流向")
        private String receiptMchId;

        @ApiModelProperty(value = "快递名称")
        private String expressName;

        @ApiModelProperty(value = "快递Code")
        private String expressCode;

        @ApiModelProperty(value = "快递单号")
        private String expressNumber;

        @ApiModelProperty(value = "0-配送+自提，1-配送，2-自提")
        private Integer sendType;

        @ApiModelProperty(value = "配送方式描述")
        @Transient
        private String sendTypeDsc;

        @ApiModelProperty(value = "自提点名称")
        private String mentionName;

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @ApiModelProperty(value = "自提点Id")
        private Long mentionId;

        @ApiModelProperty(value = "配送费用")
        private BigDecimal sendPrice = BigDecimal.ZERO;

        @ApiModelProperty(value = "积分")
        private BigDecimal integral;

        @ApiModelProperty(value = "社区币")
        private BigDecimal currency = BigDecimal.ZERO;

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @ApiModelProperty(value = "优惠卷ID")
        private Long couponId;

        @ApiModelProperty(value = "优惠卷名称")
        private String couponName;

        @ApiModelProperty(value = "平台优惠卷名称")
        private String platformCouName;

        @ApiModelProperty(value = "优惠卷资源")
        private String couponLink;

        @ApiModelProperty(value = "商家优惠卷抵扣金额")
        private BigDecimal couponPrice = BigDecimal.valueOf(0.00);

        @ApiModelProperty(value = "平台优惠卷抵扣金额")
        private BigDecimal platformPrice = BigDecimal.valueOf(0.00);

        @ApiModelProperty(value = "店铺名称")
        private String shopName;

        @ApiModelProperty(value = "店铺联系方式")
        private String shopPhone;

        @ApiModelProperty(value = "自提点联系方式")
        private String mentionPhone;

        @ApiModelProperty(value = "自提点登录账号")
        private String mentionMoblie;

        @ApiModelProperty(value = "服务经理邀请码")
        private String inviteCode;

        @ApiModelProperty(value = "自提点总佣金")
        private BigDecimal totalBrokerage = BigDecimal.ZERO;

        @ApiModelProperty(value = "分享赚总佣金")
        private BigDecimal totalShareCommission = BigDecimal.ZERO;

        @ApiModelProperty(value = "企业购订单佣金")
        private BigDecimal qygOrderCommission;

        @ApiModelProperty(value = "用户手机号")
        private String mobi;

        @ApiModelProperty(value = "拒绝原因")
        private String reasons;

        @ApiModelProperty(value = "商家拒绝用户取消申请原因")
        private String mchReasons;


        @ApiModelProperty(value = "商铺来源(0:自主注册 1:苏宁易购,网易严选 2:京东商城 3:龙马助农特色馆 4:社区人卡商城 5:积分商城 6:龙马海淘) ")
        private Integer sourceType;

        @ApiModelProperty(value = "修改时间")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date modifiedDate;

        @ApiModelProperty(value = "完成时间")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date successDate;

        @ApiModelProperty(value = "发票id")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long invoiceId;

        @ApiModelProperty(value = "订单发票信息")
        private JSONObject invoice;

        @ApiModelProperty(value = "经度")
        private String longitude;

        @ApiModelProperty(value = "纬度")
        private String latitude;

        @ApiModelProperty(value = "智慧运营佣金")
        private BigDecimal odBrokerage;

        @ApiModelProperty(value = "订单类型: 0 正常订单 1分享赚订单 2社区推手")
        private Integer orderItemType;

        @ApiModelProperty(value = "退款申请状态 0拒绝 1同意")
        private Integer refundApply;

        @ApiModelProperty(value = "erp处理状态 0待回收 1回收中 2回收成功 3回收失败")
        private Integer recycleStatus;

        @ApiModelProperty(value = "用户取消申请/分享赚退款订单标识: 1 待处理(用户取消) 2 同意取消(用户取消) 3 拒绝(用户取消) 4 待处理(用户退款) 5 同意退款(用户退款)全额退 6 同意退款(用户退款)部分退 7 拒绝退款(用户退款)")
        private String refundMark;

        @ApiModelProperty(value = "退款金额")
        private BigDecimal refundPrice;

        @ApiModelProperty(value = "1自营 2代售")
        private String type;

        @ApiModelProperty(value = "取货码")
        private String pickupCode;

        @ApiModelProperty(value = "商品下单立减金额")
        private BigDecimal reducePrice = BigDecimal.ZERO;

        @ApiModelProperty(value = "确认到店 0未到店 1 到店")
        private int confirmToShop;

        @ApiModelProperty(value = "订单是否结算 0未计算 1已结算")
        private int shareReward = 0;

        @ApiModelProperty(value = "售后申请次数")
        private Integer subRefundNum;

        @ApiModelProperty(value = "最后拒绝退款时间")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date lastRefusedRefund;

        @ApiModelProperty(value = "当前服务器时间")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date currentTime = new Date();

        @ApiModelProperty(value = "短信是否发送")
        private Integer msgType = 0;

        @ApiModelProperty(value = "是否添加备注 0 or null 无备注 1 有备注")
        private Integer isNote = 0;

        @ApiModelProperty(value = "订单子项")
        public List<OrderItem> orderItemList;

        @ApiModelProperty(value = "身份ID")
        private String identityId;

        @ApiModelProperty(value = "身份名称")
        private String identityName;

        @ApiModelProperty(value = "订单结算时间")
        private Date settlementTime;

        @ApiModelProperty(value = "订单下单渠道 1直播购买 2限时购下单 ")
        private Integer orderChannel;

        @ApiModelProperty(value = "售后状态 0 未申请售后 1 售后中 2 售后完成")
        private int afterStatus;

        @ApiModelProperty(value = "平台服务费")
        private BigDecimal platformCharge = BigDecimal.ZERO;

    }

    @Data
    public class OrderItem {
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @ApiModelProperty(value = "主键", name = "id")
        private Long id;

        @ApiModelProperty(value = "订单商品状态")
        private String status;

        @ApiModelProperty(value = "商品状态")
        private String goodStatus;

        @ApiModelProperty(value = "订单商品ID")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long goodsId;

        @ApiModelProperty(value = "商品类型：1-电子码 2-电子卡密")
        private Integer type;

        @ApiModelProperty(value = "商品条形码")
        private String code;

        @ApiModelProperty(value = "订单商品资源")
        private String goodsLink;

        @ApiModelProperty(value = "订单商品快照资源")
        private String historyLink;

        @ApiModelProperty(value = "订单商品名称")
        private String goodsName;

        @ApiModelProperty(value = "订单商品图片")
        private String goodsImg;

        @ApiModelProperty(value = "订单购买数量")
        private int goodsCount;

        @ApiModelProperty(value = "订单商品价格")
        private BigDecimal price;

        @ApiModelProperty(value = "订单商品总价")
        private BigDecimal totalPrice;

        @ApiModelProperty(value = "订单商品折扣价格")
        private BigDecimal discountPrice;

        @ApiModelProperty(value = "订单商品实付价格")
        private BigDecimal realPrice = BigDecimal.valueOf(0.00);

        @ApiModelProperty(value = "订单商品规格ID")
        private String skuId;

        @ApiModelProperty(value = "订单商品规格资源")
        private String skuLink;

        @ApiModelProperty(value = "订单商品规格名称")
        private String skuName;

        @ApiModelProperty(value = "订单商品规格/自提点 佣金")
        private BigDecimal skuBrokerage;

        @ApiModelProperty(value = "优惠卷ID")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long couponId;

        @ApiModelProperty(value = "用户ID")
        private String userId;

        @ApiModelProperty(value = "店铺ID")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long shopId;

        @ApiModelProperty(value = "优惠卷资源")
        private String couponLink;

        @ApiModelProperty(value = "优惠卷抵扣金额")
        private BigDecimal couponPrice = BigDecimal.valueOf(0.00);

        @ApiModelProperty(value = "平台优惠卷抵扣金额")
        private BigDecimal platformPrice = BigDecimal.valueOf(0.00);

        @ApiModelProperty(value = "类目id")
        private String categoryId;

        @ApiModelProperty(value = "积分")
        private BigDecimal integral;

        @ApiModelProperty(value = "社区币")
        private BigDecimal currency;

        @ApiModelProperty(value = " 购买方式（现货 0、预售 1、折扣 2、3 限时购,4 积分）")
        private Integer buyType;

        @ApiModelProperty(value = " 商品售卖来源(1 自营发卡 2 第三方发卡 3 365商品)")
        private Integer source;

        @ApiModelProperty(value = "购买次数")
        private Integer buyNum = 1;

        @ApiModelProperty(value = "分享赚  分享商铺id")
        private String shareShopId;

        @ApiModelProperty(value = "推手  分享人id")
        private String shareUserId;

        @ApiModelProperty(value = "获得的分享赚 佣金")
        private BigDecimal shareCommission = BigDecimal.ZERO;

        @ApiModelProperty(value = "每个商品设置的分享赚佣金")
        private BigDecimal goodsShareCommission = BigDecimal.ZERO;

        @ApiModelProperty(value = "退款数量")
        private Integer refundCount;

        @ApiModelProperty(value = "退款类型 1仅退款 2退款退货")
        private Integer refundType;

        @ApiModelProperty(value = "退款金额(预算值)")
        private BigDecimal refundPrice = BigDecimal.ZERO;

        @ApiModelProperty(value = "退款分享赚 佣金")
        private BigDecimal refundShareCommission;

        @ApiModelProperty(value = "商品下单立减金额")
        private BigDecimal reducePrice;

        @ApiModelProperty(value = "评价状态 0 未评价 1已评价待晒图 2已评价")
        private Integer commentStatus = 0;

        @ApiModelProperty(value = "评论获得积分")
        private Integer commentIntegral = 0;

        @ApiModelProperty(value = " 0 首评 1 追评")
        private Integer action = 0;

        @ApiModelProperty(value = "退款申请状态 0拒绝 1同意")
        private Integer refundApply;

        @ApiModelProperty(value = "拒绝原因")
        private String reasons;

        @ApiModelProperty(value = "erp处理状态 0待回收 1回收中 2回收成功 3回收失败")
        private Integer recycleStatus;

        @ApiModelProperty(value = "4 待处理(用户退款) 5 同意退款(用户退款)全额退 6 同意退款(用户退款)部分退 7 拒绝退款(用户退款) 8 退款完成 9退款申请已接受 10回收成功 11回收失败 12买家已退货")
        private String refundMark;

        @ApiModelProperty(value = "申请取消次数")
        private Integer subRefundNum;

        @ApiModelProperty(value = "拒绝退款次数")
        private Integer refRefundNum;

        @ApiModelProperty(value = "是否填写退货快递单 0 未填写 1填写 ")
        private Integer isDisplayRefund;


        @ApiModelProperty(value = "配送方式（配送+自提 0，仅配送 1，仅自提 2,默认按照店铺类型电商 仅配送，楼下小店 仅自提）")
        private Integer distributionType ;


        @ApiModelProperty(value = "最后拒绝退款时间")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date lastRefusedRefund;

        @ApiModelProperty(value = "添加时间")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date createDate;

        @ApiModelProperty(value = "订单号")
        private String orderId;

        @ApiModelProperty(value = "税费")
        private BigDecimal expenses;

        @ApiModelProperty(value = "是否展示税费")
        private Integer expensesTaxation;

        @ApiModelProperty(value = "是否展示身份信息")
        private Integer isCertification;

        @ApiModelProperty(value = "erp 是否回收 0否 1是")
        private Integer isRecycle;

        @ApiModelProperty(value = "平台服务费")
        private BigDecimal platformCharge = BigDecimal.ZERO;

        @ApiModelProperty(value = "合伙人配送费")
        private BigDecimal partnerDeliveryFee = BigDecimal.ZERO;

    }


}


