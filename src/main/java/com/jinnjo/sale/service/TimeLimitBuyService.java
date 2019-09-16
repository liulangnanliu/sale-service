package com.jinnjo.sale.service;

import com.jinnjo.sale.clients.CampaignCilent;
import com.jinnjo.sale.domain.vo.DiscountSeckillInfoVo;
import com.jinnjo.sale.domain.vo.MarketingCampaignVo;
import com.jinnjo.sale.job.SaleEndJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class TimeLimitBuyService {
    private final CampaignCilent campaignCilent;
    private final Scheduler scheduler;

    public TimeLimitBuyService(CampaignCilent campaignCilent,
                               Scheduler scheduler){
        this.campaignCilent = campaignCilent;
        this.scheduler = scheduler;
    }

    public void add(MarketingCampaignVo marketingCampaignVo){
        String campaignId = campaignCilent.addCampaigns(marketingCampaignVo);

    }

    public void executeJob() throws JobExecutionException{

        List<MarketingCampaignVo> campaignsByPages = campaignCilent.getCampaignsByPage("2019-09-10");//LocalDate.now().toString()

        List<String> goodIds = campaignsByPages.stream().map(marketingCampaignVo -> marketingCampaignVo.discountSeckillInfo).flatMap(discountSeckillInfoVo -> discountSeckillInfoVo.getSeckillGoodsList().stream()).map(seckillGoodsVo -> seckillGoodsVo.goodsSpecId).collect(Collectors.toList());

        campaignsByPages.forEach(marketingCampaignVo -> {
            DiscountSeckillInfoVo discountSeckillInfoVo = marketingCampaignVo.discountSeckillInfo;
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
