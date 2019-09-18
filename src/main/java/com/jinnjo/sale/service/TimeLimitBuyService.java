package com.jinnjo.sale.service;

import com.jinnjo.base.util.StringUtil;
import com.jinnjo.sale.clients.CampaignCilent;
import com.jinnjo.sale.clients.GoodsClient;
import com.jinnjo.sale.domain.GoodsSqr;
import com.jinnjo.sale.domain.vo.*;
import com.jinnjo.sale.job.SaleEndJob;
import com.jinnjo.sale.mq.SaleProducer;
import com.jinnjo.sale.repo.GoodsSqrRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    private final GoodsClient goodsClient;
    private final Scheduler scheduler;
    private final SaleProducer saleProducer;
    private final GoodsSqrRepository goodsSqrRepository;

    @Autowired
    public TimeLimitBuyService(CampaignCilent campaignCilent,
                               GoodsClient goodsClient,
                               Scheduler scheduler,
                               SaleProducer saleProducer,
                               GoodsSqrRepository goodsSqrRepository){
        this.campaignCilent = campaignCilent;
        this.goodsClient = goodsClient;
        this.scheduler = scheduler;
        this.saleProducer = saleProducer;
        this.goodsSqrRepository = goodsSqrRepository;
    }

    public void add(MarketingCampaignVo marketingCampaignVo){
        if(null == marketingCampaignVo)
            throw new ConstraintViolationException("新增的限时购活动不能为空!", new HashSet<>());
        Date startSeckillTime = marketingCampaignVo.getDiscountSeckillInfo().getStartSeckillTime();
        if(null == startSeckillTime)
            throw new ConstraintViolationException("新增的限时购活动时间不能为空!", new HashSet<>());

        //保存商品信息到本地
        saveGoodsInfo(marketingCampaignVo);

        String campaignsId = campaignCilent.addCampaigns(marketingCampaignVo);

        log.info("新增限时购活动营销活动返回id : " + campaignsId);

        LocalDate localDate = LocalDateTime.ofInstant(startSeckillTime.toInstant(), ZoneId.systemDefault()).toLocalDate();
        if(LocalDate.now().compareTo(localDate) == 0)//如果新增的活动商品是今天，那么直接修改商品的秒杀标识
            saleProducer.produceSend(marketingCampaignVo.getDiscountSeckillInfo().getSeckillGoodsList().stream().map(seckillGoodsVo -> new GoodsMessage(seckillGoodsVo.getGoodsId(), true)).collect(Collectors.toList()), "goods.update");


    }

    private void saveGoodsInfo(MarketingCampaignVo marketingCampaignVo){
        List<SeckillGoodsVo> seckillGoodsList = marketingCampaignVo.getDiscountSeckillInfo().getSeckillGoodsList();

        //查询已存在的商品实例id
        List<String> localGoodIds = goodsSqrRepository.queryAllByIdNotIn(seckillGoodsList.stream().map(seckillGoodsVo -> seckillGoodsVo.getGoodsId()).collect(Collectors.toList())).stream().map(t -> t.toString()).collect(Collectors.toList());

        //查询商品信息
        String goodIds = seckillGoodsList.stream().map(seckillGoodsVo -> String.valueOf(seckillGoodsVo.getGoodsId())).filter(t -> !localGoodIds.contains(t)).collect(Collectors.joining(","));

        if(StringUtil.isEmpty(goodIds))
            return;

        Collection<GoodsSqr> goods = goodsClient.findGoods(seckillGoodsList.size(), "id::" + goodIds).getContent();
        goodsSqrRepository.saveAll(goods);
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
            throw new ConstraintViolationException("当天限时购活动时间不能修改!", new HashSet<>());
        }

        campaignCilent.updateCampaign(marketingCampaignVo.getId(), marketingCampaignVo);
    }

    public void delete(Long id){
        campaignCilent.deleteCampaign(id);
    }

    public void changeStatus(Long id, Integer status){
        campaignCilent.changeStatus(id, status);
    }

    public Page<MarketingCampaignVo> getTimeLimitBuy(Integer page, Integer size , String startSeckillTime , String endSeckillTime, Integer status){
        PageVo<MarketingCampaignVo> pageVo = campaignCilent.getSeckillByPage(startSeckillTime , endSeckillTime, page, size , status);

        List<MarketingCampaignVo> content = pageVo.getContent().stream().map(marketingCampaignVo -> {
            long time = new Date().getTime();
            if (time < marketingCampaignVo.getDiscountSeckillInfo().getStartSeckillTime().getTime())
                marketingCampaignVo.setTimeLimitStatus(0);
            else if (time >= marketingCampaignVo.getDiscountSeckillInfo().getStartSeckillTime().getTime() && time <= marketingCampaignVo.getDiscountSeckillInfo().getEndSeckillTime().getTime())
                marketingCampaignVo.setTimeLimitStatus(2 == marketingCampaignVo.getStatus() ? 3 : 1);
            else if (time > marketingCampaignVo.getDiscountSeckillInfo().getEndSeckillTime().getTime())
                marketingCampaignVo.setTimeLimitStatus(3);
            return marketingCampaignVo;
        }).collect(Collectors.toList());

        return new PageImpl<>(content, PageRequest.of(page, size), pageVo.getTotalElements());
    }

    public Long checkSeckillGoods(String date, Long goodsId){
        return campaignCilent.checkSeckillGoods(date, goodsId);
    }

    public void executeJob() throws JobExecutionException{

        List<MarketingCampaignVo> campaignsByPages = campaignCilent.getCampaignsByPage(LocalDate.now().toString());

        List<Long> goodIds = campaignsByPages.stream().map(marketingCampaignVo -> marketingCampaignVo.getDiscountSeckillInfo()).flatMap(discountSeckillInfoVo -> discountSeckillInfoVo.getSeckillGoodsList().stream()).map(seckillGoodsVo -> seckillGoodsVo.getGoodsId()).collect(Collectors.toList());

        //今天所有的商品标识为限时购商品
        saleProducer.produceSend(goodIds.stream().map(goodId -> new GoodsMessage(goodId, true)).collect(Collectors.toList()), "goods.update");

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
        List<Long> goodIds = seckillGoods.stream().map(seckillGoodsVo -> seckillGoodsVo.getGoodsId()).collect(Collectors.toList());
        //将所有商品的限时购标识取消
        saleProducer.produceSend(goodIds.stream().map(goodId -> new GoodsMessage(goodId, false)).collect(Collectors.toList()), "goods.update");

    }
}
