package com.jinnjo.sale.service;

import com.jinnjo.sale.clients.CampaignCilent;
import com.jinnjo.sale.domain.TimeLimitBuy;
import com.jinnjo.sale.domain.vo.DiscountSeckillInfoVo;
import com.jinnjo.sale.domain.vo.MarketingCampaignVo;
import com.jinnjo.sale.domain.vo.SeckillGoodsVo;
import com.jinnjo.sale.domain.vo.TimeLimitBuyVo;
import com.jinnjo.sale.job.SaleEndJob;
import com.jinnjo.sale.repo.TimeLimitBuyRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class TimeLimitBuyService {
    private final CampaignCilent campaignCilent;
    private final TimeLimitBuyRepository timeLimitBuyRepository;
    private final Scheduler scheduler;

    public TimeLimitBuyService(CampaignCilent campaignCilent,
                               TimeLimitBuyRepository timeLimitBuyRepository,
                               Scheduler scheduler){
        this.campaignCilent = campaignCilent;
        this.timeLimitBuyRepository = timeLimitBuyRepository;
        this.scheduler = scheduler;
    }

    public void add(TimeLimitBuyVo timeLimitBuyVo){
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

        List<TimeLimitBuy> timeLimitBuys = timeLimitBuyVo.getGoodInfoVos().stream().map(goodInfo ->  new TimeLimitBuy(saleTime,campaignId, goodInfo)).collect(Collectors.toList());
        timeLimitBuyRepository.saveAll(timeLimitBuys);
    }

    public void executeJob() throws JobExecutionException{

        List<MarketingCampaignVo> campaignsByPages = campaignCilent.getCampaignsByPage("2019-09-10");//LocalDate.now().toString()

        List<String> goodIds = campaignsByPages.stream().map(marketingCampaignVo -> marketingCampaignVo.discountSeckillInfoVo).flatMap(discountSeckillInfoVo -> discountSeckillInfoVo.getSeckillGoodsList().stream()).map(seckillGoodsVo -> seckillGoodsVo.goodsSpecId).collect(Collectors.toList());

        campaignsByPages.forEach(marketingCampaignVo -> {
            DiscountSeckillInfoVo discountSeckillInfoVo = marketingCampaignVo.discountSeckillInfoVo;
            if(null == discountSeckillInfoVo)
                return;

            startSaleEndJob(discountSeckillInfoVo.endSeckillTime);
        });

    }

    private void startSaleEndJob(Date endSeckillTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        JobDetail jobDetail = JobBuilder.newJob(SaleEndJob.class).withIdentity("saleEndJob" + sdf.format(endSeckillTime), "saleGroup").usingJobData("endTime", sdf.format(endSeckillTime)).build();
        Trigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail).startAt(endSeckillTime).build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        }catch (SchedulerException e){
            e.printStackTrace();
            log.error("saleEndJob执行异常");
        }
    }
}
