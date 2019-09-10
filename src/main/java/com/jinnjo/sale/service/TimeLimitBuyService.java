package com.jinnjo.sale.service;

import com.jinnjo.sale.clients.CampaignCilent;
import com.jinnjo.sale.domain.TimeLimitBuy;
import com.jinnjo.sale.domain.vo.DiscountSeckillInfoVo;
import com.jinnjo.sale.domain.vo.MarketingCampaignVo;
import com.jinnjo.sale.domain.vo.SeckillGoodsVo;
import com.jinnjo.sale.domain.vo.TimeLimitBuyVo;
import com.jinnjo.sale.repo.TimeLimitBuyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TimeLimitBuyService {
    private final CampaignCilent campaignCilent;
    private final TimeLimitBuyRepository timeLimitBuyRepository;

    public TimeLimitBuyService(CampaignCilent campaignCilent,
                               TimeLimitBuyRepository timeLimitBuyRepository){
        this.campaignCilent = campaignCilent;
        this.timeLimitBuyRepository = timeLimitBuyRepository;
    }

    public List<TimeLimitBuy> add(TimeLimitBuyVo timeLimitBuyVo){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime, endTime, saleTime;
        try {
            startTime = sdf.parse(timeLimitBuyVo.getSaleTime() +" "+ timeLimitBuyVo.getStartSeckillTime()+":00");
            endTime = sdf.parse(timeLimitBuyVo.getSaleTime() +" "+ timeLimitBuyVo.getEndSeckillTime()+":00");
            saleTime = new SimpleDateFormat("yyyy-MM-dd").parse(timeLimitBuyVo.getSaleTime());
        }catch (Exception e){
            throw new ConstraintViolationException("时间格式错误", new HashSet<>());
        }

        List<SeckillGoodsVo> seckillGoodsList = timeLimitBuyVo.getGoodInfoVos().stream().map(SeckillGoodsVo::new).collect(Collectors.toList());

        MarketingCampaignVo marketingCampaignVo = new MarketingCampaignVo("", startTime, endTime, new DiscountSeckillInfoVo(startTime, endTime, seckillGoodsList));
        String campaignId = campaignCilent.addCampaigns(marketingCampaignVo);

        List<TimeLimitBuy> timeLimitBuys = timeLimitBuyVo.getGoodInfoVos().stream().map(goodInfo ->  new TimeLimitBuy(saleTime, goodInfo)).collect(Collectors.toList());
        return timeLimitBuyRepository.saveAll(timeLimitBuys);
    }

    public static void main(String[] args) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.parse("2019-09-10 10:00:00"));
    }
}
