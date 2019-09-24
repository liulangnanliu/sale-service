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
        Date endSeckillTime = marketingCampaignVo.getDiscountSeckillInfo().getEndSeckillTime();
        if(null == startSeckillTime || null == endSeckillTime)
            throw new ConstraintViolationException("新增的限时购活动时间不能为空!", new HashSet<>());

        if(startSeckillTime.compareTo(endSeckillTime) >= 0)
            throw new ConstraintViolationException("新增的限时购活动开始时间不能大于结束时间!", new HashSet<>());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Long timeFlag = campaignCilent.checkSeckillTime(sdf.format(startSeckillTime), sdf.format(endSeckillTime), null);
        if(timeFlag == 1)
            throw new ConstraintViolationException("新增的限时购活动时间已经存在或者存在重复时间段!", new HashSet<>());

        LocalDate seckillTime = LocalDateTime.ofInstant(startSeckillTime.toInstant(), ZoneId.systemDefault()).toLocalDate();

        String goodsIds = marketingCampaignVo.getDiscountSeckillInfo().getSeckillGoodsList().stream().map(seckillGoodsVo -> String.valueOf(seckillGoodsVo.getGoodsId())).collect(Collectors.joining(","));
        List<String> seckillGoodsList = campaignCilent.checkSeckillGoodsList(seckillTime.toString(), goodsIds);
        if(seckillGoodsList.size() > 0)
            throw new ConstraintViolationException("新增的限时购活动商品"+seckillGoodsList.stream().collect(Collectors.joining(","))+"已存在!", new HashSet<>());


        saveAndValidateGoodsInfo(marketingCampaignVo);

        String campaignsId = campaignCilent.addCampaigns(marketingCampaignVo);

        log.info("新增限时购活动营销活动返回id : " + campaignsId);

        LocalDate localDate = LocalDateTime.ofInstant(startSeckillTime.toInstant(), ZoneId.systemDefault()).toLocalDate();
        if(LocalDate.now().compareTo(localDate) == 0) {
            //如果新增的活动商品是今天，那么直接修改商品的秒杀标识
            saleProducer.produceSend(marketingCampaignVo.getDiscountSeckillInfo().getSeckillGoodsList().stream().map(seckillGoodsVo -> new GoodsMessage(seckillGoodsVo.getGoodsId(), true)).collect(Collectors.toList()), "goods.update");
            //活动结束时，将商品的限时购标识清空
            startSaleEndJob(startSeckillTime, endSeckillTime);
        }


    }

    private List<GoodsSqr> saveAndValidateGoodsInfo(MarketingCampaignVo marketingCampaignVo){
        //保存商品信息到本地
        List<GoodsSqr> goodsSqrs = saveGoodsInfo(marketingCampaignVo);

        //校验商品的价格
        String goodsName = marketingCampaignVo.getDiscountSeckillInfo().getSeckillGoodsList().stream().filter(seckillGoodsVo -> {
            return goodsSqrs.stream().flatMap(goodsSqr -> goodsSqr.getSkuInfos().stream()).anyMatch(goodsSkuSqr -> {
                if(goodsSkuSqr.getId().equals(seckillGoodsVo.getGoodsSpecId())) {
                    if (null != goodsSkuSqr.getDiscountPrice()) {
                        return (seckillGoodsVo.getSeckillPrice().compareTo(goodsSkuSqr.getDiscountPrice()) >= 0) ? true : false;
                    } else {
                        return (seckillGoodsVo.getSeckillPrice().compareTo(goodsSkuSqr.getPrice()) >= 0) ? true : false;
                    }
                }
                return false;
            });
        }).map(seckillGoodsVo -> seckillGoodsVo.getGoodsName()).collect(Collectors.joining(","));
        if(StringUtil.isNotEmpty(goodsName))
            throw new ConstraintViolationException("新增的限时购活动商品"+goodsName+"价格不能大于原先价格!", new HashSet<>());

        return goodsSqrs;
    }

    private List<GoodsSqr> saveGoodsInfo(MarketingCampaignVo marketingCampaignVo){
        List<SeckillGoodsVo> seckillGoodsList = marketingCampaignVo.getDiscountSeckillInfo().getSeckillGoodsList();

        //查询已存在的商品实例id
        List<GoodsSqr> goodsSqrList = goodsSqrRepository.queryAllByIdIn(seckillGoodsList.stream().map(seckillGoodsVo -> seckillGoodsVo.getGoodsId()).collect(Collectors.toList()));
        List<String> localGoodIds = goodsSqrList.stream().map(t -> t.getId().toString()).collect(Collectors.toList());

        //查询商品信息
        String goodIds = seckillGoodsList.stream().map(seckillGoodsVo -> String.valueOf(seckillGoodsVo.getGoodsId())).filter(t -> !localGoodIds.contains(t)).collect(Collectors.joining(","));

        if(StringUtil.isEmpty(goodIds))
            return goodsSqrList;

        Collection<GoodsSqr> goods = goodsClient.findGoods(seckillGoodsList.size(), "id::" + goodIds).getContent();
        List<GoodsSqr> goodsSqrs = goodsSqrRepository.saveAll(goods);
        goodsSqrList.addAll(goodsSqrs);
        return goodsSqrList;
    }

    public void update(MarketingCampaignVo marketingCampaignVo){
        if(null == marketingCampaignVo)
            throw new ConstraintViolationException("修改的限时购活动不能为空!", new HashSet<>());

        if(StringUtil.isEmpty(marketingCampaignVo.getId()))
            throw new ConstraintViolationException("限时购活动id不能为空!", new HashSet<>());


        Date startSeckillTime = marketingCampaignVo.getDiscountSeckillInfo().getStartSeckillTime();
        Date endSeckillTime = marketingCampaignVo.getDiscountSeckillInfo().getEndSeckillTime();
        if(null == startSeckillTime || null == endSeckillTime)
            throw new ConstraintViolationException("修改的限时购活动时间不能为空!", new HashSet<>());

        if(startSeckillTime.compareTo(endSeckillTime) >= 0)
            throw new ConstraintViolationException("修改的限时购活动开始时间不能大于结束时间!", new HashSet<>());


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        LocalDate localDate = LocalDateTime.ofInstant(startSeckillTime.toInstant(), ZoneId.systemDefault()).toLocalDate();
        MarketingCampaignVo target = campaignCilent.getCampaignById(Long.parseLong(marketingCampaignVo.getId()));
        if(LocalDate.now().compareTo(localDate) == 0){//如果新增的活动商品是今天，那么直接修改商品的秒杀标识
            if(!target.getDiscountSeckillInfo().getStartSeckillTime().equals(startSeckillTime)
                    || !target.getDiscountSeckillInfo().getEndSeckillTime().equals(endSeckillTime))
                throw new ConstraintViolationException("当天限时购活动时间不能修改!", new HashSet<>());
        }else{
            //校验活动时间
            if(!target.getDiscountSeckillInfo().getStartSeckillTime().equals(startSeckillTime)
                    || !target.getDiscountSeckillInfo().getEndSeckillTime().equals(endSeckillTime)){
                Long timeFlag = campaignCilent.checkSeckillTime(sdf.format(startSeckillTime), sdf.format(endSeckillTime), marketingCampaignVo.getId());
                if(timeFlag == 1)
                    throw new ConstraintViolationException("修改的限时购活动时间已经存在或者存在重复时间段!", new HashSet<>());
            }
        }

        saveAndValidateGoodsInfo(marketingCampaignVo);

        //校验商品
        List<Long> targetGoodsIds = target.getDiscountSeckillInfo().getSeckillGoodsList().stream().map(seckillGoodsVo -> seckillGoodsVo.getGoodsId()).collect(Collectors.toList());
        String newGoodsIds = marketingCampaignVo.getDiscountSeckillInfo().getSeckillGoodsList().stream().filter(t -> !targetGoodsIds.contains(t.getGoodsId())).map(seckillGoodsVo -> String.valueOf(seckillGoodsVo.getGoodsId())).collect(Collectors.joining(","));
        if(StringUtil.isNotEmpty(newGoodsIds)){
            LocalDate seckillTime = LocalDateTime.ofInstant(startSeckillTime.toInstant(), ZoneId.systemDefault()).toLocalDate();
            List<String> seckillGoodsList = campaignCilent.checkSeckillGoodsList(seckillTime.toString(), newGoodsIds);
            if(seckillGoodsList.size() > 0)
                throw new ConstraintViolationException("修改的限时购活动商品"+seckillGoodsList.stream().collect(Collectors.joining(","))+"已存在!", new HashSet<>());
        }


        campaignCilent.updateCampaign(marketingCampaignVo.getId(), marketingCampaignVo);

        if(LocalDate.now().compareTo(localDate) == 0 && StringUtil.isNotEmpty(newGoodsIds)) {
            //如果新增的活动商品是今天，那么直接修改商品的秒杀标识
            saleProducer.produceSend(marketingCampaignVo.getDiscountSeckillInfo().getSeckillGoodsList().stream().filter(t -> !targetGoodsIds.contains(t.getGoodsId()))
                                                                                                                .map(seckillGoodsVo -> new GoodsMessage(seckillGoodsVo.getGoodsId(), true))
                                                                                                                .collect(Collectors.toList())
                                    , "goods.update");
        }
    }

    public void delete(Long id){
        campaignCilent.deleteCampaign(id);
    }

    public void deleteGoods(Long id, Long goodsId){
        campaignCilent.deleteGoods(id, goodsId);
    }

    public void changeStatus(Long id, Integer status){
        campaignCilent.changeStatus(id, status);
    }

    public MarketingCampaignVo getTimeLimitBuyById(Long id){
        return campaignCilent.getCampaignById(id);
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

    public Long checkSeckillGoods(String date, Long goodsId, Long goodsSpecId){
        return campaignCilent.checkSeckillGoods(date, goodsId, goodsSpecId);
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
