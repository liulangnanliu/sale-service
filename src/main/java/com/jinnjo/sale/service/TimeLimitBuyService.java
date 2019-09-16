package com.jinnjo.sale.service;

import com.jinnjo.base.util.StringUtil;
import com.jinnjo.sale.clients.CampaignCilent;
import com.jinnjo.sale.domain.vo.DiscountSeckillInfoVo;
import com.jinnjo.sale.domain.vo.MarketingCampaignVo;
import com.jinnjo.sale.domain.vo.SeckillGoodsVo;
import com.jinnjo.sale.job.SaleEndJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
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
        if(null == marketingCampaignVo)
            throw new ConstraintViolationException("新增的限时购活动不能为空!", new HashSet<>());
        Date startSeckillTime = marketingCampaignVo.getDiscountSeckillInfo().getStartSeckillTime();
        if(null == startSeckillTime)
            throw new ConstraintViolationException("新增的限时购活动时间不能为空!", new HashSet<>());
        LocalDate localDate = LocalDateTime.ofInstant(startSeckillTime.toInstant(), ZoneId.systemDefault()).toLocalDate();
        if(LocalDate.now().compareTo(localDate) == 0){//如果新增的活动商品是今天，那么直接修改商品的秒杀标识

        }

        campaignCilent.addCampaigns(marketingCampaignVo);
    }

    public void update(MarketingCampaignVo marketingCampaignVo){
        if(null == marketingCampaignVo)
            throw new ConstraintViolationException("修改的限时购活动不能为空!", new HashSet<>());

        if(StringUtil.isEmpty(marketingCampaignVo.getId()))
            throw new ConstraintViolationException("限时购活动id不能为空!", new HashSet<>());


        Date startSeckillTime = marketingCampaignVo.getDiscountSeckillInfo().getStartSeckillTime();
        if(null == startSeckillTime)
            throw new ConstraintViolationException("限时购活动时间不能为空!", new HashSet<>());
        LocalDate localDate = LocalDateTime.ofInstant(startSeckillTime.toInstant(), ZoneId.systemDefault()).toLocalDate();
        if(LocalDate.now().compareTo(localDate) == 0){//如果新增的活动商品是今天，那么直接修改商品的秒杀标识
            MarketingCampaignVo target = campaignCilent.getCampaignById(marketingCampaignVo.getId());
            if(!target.getDiscountSeckillInfo().getStartSeckillTime().equals(marketingCampaignVo.getDiscountSeckillInfo().getStartSeckillTime())
                    || !target.getDiscountSeckillInfo().getEndSeckillTime().equals(marketingCampaignVo.getDiscountSeckillInfo().getEndSeckillTime()))
            throw new ConstraintViolationException("当天限时购活动时间不能为空!", new HashSet<>());
        }

        campaignCilent.addCampaigns(marketingCampaignVo);
    }

    public void executeJob() throws JobExecutionException{

        List<MarketingCampaignVo> campaignsByPages = campaignCilent.getCampaignsByPage("2019-09-10");//LocalDate.now().toString()

        List<String> goodIds = campaignsByPages.stream().map(marketingCampaignVo -> marketingCampaignVo.getDiscountSeckillInfo()).flatMap(discountSeckillInfoVo -> discountSeckillInfoVo.getSeckillGoodsList().stream()).map(seckillGoodsVo -> seckillGoodsVo.getGoodsSpecId()).collect(Collectors.toList());

        //今天所有的商品标识为限时购商品


        campaignsByPages.forEach(marketingCampaignVo -> {
            DiscountSeckillInfoVo discountSeckillInfoVo = marketingCampaignVo.getDiscountSeckillInfo();
            if(null == discountSeckillInfoVo)
                return;

            //活动结束时，将商品的限时购标识清空
            startSaleEndJob(discountSeckillInfoVo.getStartSeckillTime(), discountSeckillInfoVo.getEndSeckillTime());
        });

    }

    private void startSaleEndJob(Date startSeckillTime, Date endSeckillTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        JobDetail jobDetail = JobBuilder.newJob(SaleEndJob.class).withIdentity("saleEndJob" + sdf.format(endSeckillTime), "saleGroup").usingJobData("startTime", sdf.format(startSeckillTime)).usingJobData("endTime", sdf.format(endSeckillTime)).build();
        Trigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail).startAt(endSeckillTime).build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        }catch (SchedulerException e){
            e.printStackTrace();
            log.error("saleEndJob执行异常");
        }
    }

    public void executeSaleEndJob(String startSeckillTime, String endSeckillTime){
        List<SeckillGoodsVo> seckillGoods = campaignCilent.getGoodsListBySeckTime(startSeckillTime, endSeckillTime);
        List<String> goodIds = seckillGoods.stream().map(seckillGoodsVo -> seckillGoodsVo.getGoodsSpecId()).collect(Collectors.toList());
        //将所有商品的限时购标识取消
    }
}
