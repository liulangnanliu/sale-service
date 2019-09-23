package com.jinnjo.sale.service;

import com.jinnjo.base.util.StringUtil;
import com.jinnjo.sale.clients.CampaignCilent;
import com.jinnjo.sale.clients.OrderClient;
import com.jinnjo.sale.domain.GoodsSkuSqr;
import com.jinnjo.sale.domain.GoodsSqr;
import com.jinnjo.sale.domain.vo.*;
import com.jinnjo.sale.repo.GoodsSqrRepository;
import com.jinnjo.sale.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Transactional
@Service
public class TimeLimitBuyOrderService {

    private final CampaignCilent campaignCilent;

    private final OrderClient orderClient;

    private final GoodsSqrRepository goodsSqrRepository;

    private final StringRedisTemplate stringRedisTemplate17;

    @Autowired
    public TimeLimitBuyOrderService(CampaignCilent campaignCilent,
                                    OrderClient orderClient,
                                    GoodsSqrRepository goodsSqrRepository,
                                    @Qualifier("redis17") StringRedisTemplate stringRedisTemplate17){
        this.campaignCilent = campaignCilent;
        this.orderClient = orderClient;
        this.goodsSqrRepository = goodsSqrRepository;
        this.stringRedisTemplate17 = stringRedisTemplate17;
    }

    public OrderUnifiedVo add(OrderSubmitVo orderSubmitVo){
        //只考虑单商品购买
        OrderItemVo orderItemVo = orderSubmitVo.getVoList().get(0).getOrderItemVOList().get(0);

        MarketingCampaignVo campaignVo = campaignCilent.getCampaignsByGoodsId(LocalDate.now().toString(), orderItemVo.getGoodsId());

        if(null == campaignVo)
            return orderClient.orders(orderSubmitVo);

        //校验当前的商品规格是否是限时购商品规格
        SeckillGoodsVo seckillGoods = campaignVo.getDiscountSeckillInfo().getSeckillGoodsList().stream().filter(seckillGoodsVo -> orderItemVo.getGoodsId().equals(seckillGoodsVo.getGoodsId()) && orderItemVo.getSkuId().equals(seckillGoodsVo.getGoodsSpecId()))
                .findFirst()
                .orElse(null);

        if(null == seckillGoods)
            return orderClient.orders(orderSubmitVo);

        //校验当前的时间是否是限时购活动时间
        if(new Date().getTime() < campaignVo.getDiscountSeckillInfo().getStartSeckillTime().getTime() || new Date().getTime() > campaignVo.getDiscountSeckillInfo().getEndSeckillTime().getTime())
            return orderClient.orders(orderSubmitVo);

        //校验用户的限购数量
        String buyCount = null;
        if(null != seckillGoods.getLimitCount()){ //null为不限购
            buyCount = stringRedisTemplate17.opsForValue().get("timeLimitUser" + UserUtil.getCurrentUserId() + "_" + orderItemVo.getGoodsId());
            if(StringUtil.isNotEmpty(buyCount) && (Integer.parseInt(buyCount) + orderItemVo.getGoodsCount()) > seckillGoods.getLimitCount())
                throw new ConstraintViolationException("用户今天累计购买数量已经超过限时购商品限购数量!", new HashSet<>());
        }

        fillOrderItemVo(orderItemVo, seckillGoods);
        //orderItemVo.setStock(goodsSkuSqr.getStock());

        OrderUnifiedVo orderUnifiedVo = orderClient.limitTimeOrder(orderSubmitVo);

        if(null != seckillGoods.getLimitCount()) {
            buyCount = (orderItemVo.getGoodsCount() + (StringUtil.isEmpty(buyCount) ? 0 : Integer.parseInt(buyCount))) + "";
            stringRedisTemplate17.opsForValue().set("timeLimitUser" + UserUtil.getCurrentUserId() + "_" + orderItemVo.getGoodsId(), buyCount, ChronoUnit.SECONDS.between(LocalDateTime.now(), LocalDateTime.now().withHour(23).withMinute(59).withSecond(59)), TimeUnit.SECONDS);
        }
        return orderUnifiedVo;
    }

    public Map<String, Object> getShoppingFee(OrderSubmitVo orderSubmitVo){
        //只考虑单商品购买
        OrderItemVo orderItemVo = orderSubmitVo.getVoList().get(0).getOrderItemVOList().get(0);


        MarketingCampaignVo campaignVo = campaignCilent.getCampaignsByGoodsId(LocalDate.now().toString(), orderItemVo.getGoodsId());

        if(null == campaignVo)
            return orderClient.getShoppingFee(orderSubmitVo, 0);

        //校验当前的商品规格是否是限时购商品规格
        SeckillGoodsVo seckillGoods = campaignVo.getDiscountSeckillInfo().getSeckillGoodsList().stream().filter(seckillGoodsVo -> orderItemVo.getGoodsId().equals(seckillGoodsVo.getGoodsId()) && orderItemVo.getSkuId().equals(seckillGoodsVo.getGoodsSpecId()))
                .findFirst()
                .orElse(null);

        if(null == seckillGoods)
            return orderClient.getShoppingFee(orderSubmitVo, 0);

        //校验当前的时间是否是限时购活动时间
        if(new Date().getTime() < campaignVo.getDiscountSeckillInfo().getStartSeckillTime().getTime() || new Date().getTime() > campaignVo.getDiscountSeckillInfo().getEndSeckillTime().getTime())
            return orderClient.getShoppingFee(orderSubmitVo, 0);

        fillOrderItemVo(orderItemVo, seckillGoods);
        return orderClient.getShoppingFee(orderSubmitVo, 1);
    }

    private void fillOrderItemVo(OrderItemVo orderItemVo, SeckillGoodsVo seckillGoods){
        orderItemVo.setDiscountPrice(seckillGoods.getSeckillPrice()); //商品折扣价设置为秒杀价格

        GoodsSqr goodsSqr = goodsSqrRepository.findByIdAndStatus(orderItemVo.getGoodsId(), 3);
        if(null == goodsSqr)
            throw new ConstraintViolationException("商品被删除或下架!", new HashSet<>());

        orderItemVo.setType(goodsSqr.getType());
        orderItemVo.setBuyType(goodsSqr.getBuyType());
        orderItemVo.setExpenses(goodsSqr.getExpenses());
        orderItemVo.setExpensesTaxation(goodsSqr.getExpensesTaxation());
        orderItemVo.setIsCertification(StringUtil.isEmpty(goodsSqr.getIsCertification()) ? null : Integer.parseInt(goodsSqr.getIsCertification()));
        orderItemVo.setPlatformCharge(goodsSqr.getPlatformCharge());
        orderItemVo.setTsfGoodsId(String.valueOf(goodsSqr.getTsfGoodsId()));
        orderItemVo.setDistributionType(goodsSqr.getDistributionType());
        orderItemVo.setIsShareGoods(goodsSqr.getIsShareGoods());
        orderItemVo.setIsIntegralGoods(goodsSqr.getIsIntegralGoods());
        orderItemVo.setBonus(goodsSqr.getBonus());
        orderItemVo.setCommission(goodsSqr.getCommission());
        orderItemVo.setTitle(goodsSqr.getTitle());
        orderItemVo.setIcon(goodsSqr.getIcon());
        orderItemVo.setSource(goodsSqr.getSource());

        GoodsSkuSqr goodsSkuSqr = goodsSqr.getSkuInfos().stream().filter(skuInfo -> skuInfo.getId().equals(orderItemVo.getSkuId())).findFirst().orElse(null);
        if(null == goodsSkuSqr)
            throw new ConstraintViolationException(orderItemVo.getSkuId() + "规格不存在!", new HashSet<>());

        orderItemVo.setPrice(goodsSkuSqr.getPrice());
        orderItemVo.setSpStrVal(goodsSkuSqr.getSpStrVal());
    }

}
