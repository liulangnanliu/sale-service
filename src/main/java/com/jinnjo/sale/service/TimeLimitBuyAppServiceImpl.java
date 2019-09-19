package com.jinnjo.sale.service;

import com.jinnjo.sale.clients.BmsClient;
import com.jinnjo.sale.clients.CampaignCilent;
import com.jinnjo.sale.domain.GoodsSkuSqr;
import com.jinnjo.sale.domain.vo.*;
import com.jinnjo.sale.repo.GoodsSkuSqrRepository;
import com.jinnjo.sale.utils.SignatureUtil;
import com.jinnjo.sale.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author bobo
 * @date 2019\9\10 0010 16:11
 * description:
 */
@Service
@Slf4j
public class TimeLimitBuyAppServiceImpl implements TimeLimitBuyAppService{
    private final CampaignCilent campaignCilent;
    private final GoodsSkuSqrRepository goodsSkuSqrRepository;
    private final BmsClient bmsClient;

    @Autowired
    public TimeLimitBuyAppServiceImpl(CampaignCilent campaignCilent,
                                      GoodsSkuSqrRepository goodsSkuSqrRepository,
                                      BmsClient bmsClient){
        this.campaignCilent = campaignCilent;
        this.goodsSkuSqrRepository = goodsSkuSqrRepository;
        this.bmsClient = bmsClient;
    }

    @Override
    public MarketingCampaignVo getForTop() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());
        List<MarketingCampaignVo> marketingCampaignVoList = campaignCilent.getCampaignsByPage(date);
        if (marketingCampaignVoList.size()<=0){
            return null;
        }
        List<MarketingCampaignVo> marketingCampaignVoArrayList = new ArrayList<>();
        List<MarketingCampaignVo> finallyList = new ArrayList<>();
        for (MarketingCampaignVo marketingCampaignVo:marketingCampaignVoList){
            DiscountSeckillInfoVo discountSeckillInfoVo = marketingCampaignVo.getDiscountSeckillInfo();
            if (null!=discountSeckillInfoVo){
                Date startTime = discountSeckillInfoVo.getStartSeckillTime();
                Date endTime = discountSeckillInfoVo.getEndSeckillTime();
                Date nowTime = new Date();
                if (startTime.before(nowTime)&&endTime.after(nowTime)){
                    //当前时间在活动时间段内
                    List<SeckillGoodsVo> seckillGoodsVoList = discountSeckillInfoVo.getSeckillGoodsList();
                    seckillGoodsVoList.removeIf(seckillGoodsVo -> seckillGoodsVo.getStatus().equals(4));
                    if (seckillGoodsVoList.size()>2){
                        discountSeckillInfoVo.setSeckillGoodsList(seckillGoodsVoList.subList(0,3));
                    }
                    marketingCampaignVo.setTimeLimitStatus(1);
                    return marketingCampaignVo;
                }
                if (endTime.after(nowTime)&&startTime.after(nowTime)){
                    //当前时间在活动时间段之前
                    Long start = startTime.getTime();
                    Long now = nowTime.getTime();
                    Long interval = start - now;
                    marketingCampaignVo.setInterval(interval);
                    List<SeckillGoodsVo> seckillGoodsVoList = discountSeckillInfoVo.getSeckillGoodsList();
                    seckillGoodsVoList.removeIf(seckillGoodsVo -> seckillGoodsVo.getStatus().equals(4));
                    if (seckillGoodsVoList.size()>2){
                        discountSeckillInfoVo.setSeckillGoodsList(seckillGoodsVoList.subList(0,3));
                    }
                    marketingCampaignVo.setTimeLimitStatus(0);
                    marketingCampaignVoArrayList.add(marketingCampaignVo);
                }
                if (endTime.before(nowTime)){
                    //当前时间在活动时间段之后
                    List<SeckillGoodsVo> seckillGoodsVoList = discountSeckillInfoVo.getSeckillGoodsList();
                    seckillGoodsVoList.removeIf(seckillGoodsVo -> seckillGoodsVo.getStatus().equals(4));
                    if (seckillGoodsVoList.size()>2){
                        discountSeckillInfoVo.setSeckillGoodsList(seckillGoodsVoList.subList(0,3));
                    }
                    marketingCampaignVo.setTimeLimitStatus(3);
                    finallyList.add(marketingCampaignVo);
                }
            }
        }
        if (marketingCampaignVoArrayList.size()>0){
            return marketingCampaignVoArrayList.stream().min(Comparator.comparing(MarketingCampaignVo::getInterval)).orElse(null);
        }
        return finallyList.stream().max(Comparator.comparing(MarketingCampaignVo::getEndTime)).orElse(null);
    }

    @Override
    @SuppressWarnings("all")
    public Page<SeckillGoodsVo> getById(Long id,Integer page,Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        MarketingCampaignVo marketingCampaignVo = campaignCilent.getCampaignById(id);
        List<SeckillGoodsVo> seckillGoodsVoList = marketingCampaignVo.getDiscountSeckillInfo().getSeckillGoodsList();
        List<Long> skuIdList = new ArrayList<>();
        seckillGoodsVoList.forEach(seckillGoodsVo -> skuIdList.add(seckillGoodsVo.getGoodsSpecId()));
        List<GoodsSkuSqr> goodsSkuSqrList = goodsSkuSqrRepository.findByIdIn(skuIdList);
        seckillGoodsVoList.forEach(seckillGoodsVo -> {
            goodsSkuSqrList.forEach(goodsSkuSqr -> {
                if (seckillGoodsVo.getGoodsSpecId().equals(goodsSkuSqr.getId())){
                    seckillGoodsVo.setStock(goodsSkuSqr.getStock());
                }
            });
        });
        int total = seckillGoodsVoList.size();
        Page<SeckillGoodsVo> seckillGoodsVoPage = new PageImpl(seckillGoodsVoList.subList(page*size,total<(page+1)*size?total:(page+1)*size),pageable,total);
        return seckillGoodsVoPage;
    }

    @Override
    public List<MarketingCampaignVo> getForList() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());
        List<MarketingCampaignVo> marketingCampaignVoList = campaignCilent.getCampaignsByPage(date);
        if (marketingCampaignVoList.size()<=0){
            return null;
        }
        for (MarketingCampaignVo marketingCampaignVo:marketingCampaignVoList){
            DiscountSeckillInfoVo discountSeckillInfoVo = marketingCampaignVo.getDiscountSeckillInfo();
            if (null!=discountSeckillInfoVo){
                Date startTime = discountSeckillInfoVo.getStartSeckillTime();
                Date endTime = discountSeckillInfoVo.getEndSeckillTime();
                Date nowTime = new Date();
                if (startTime.after(nowTime)){
                    //未开启
                    marketingCampaignVo.setTimeLimitStatus(0);
                }
                if (startTime.before(nowTime)&&endTime.after(nowTime)){
                    //当前时间在活动时间段内
                    if (marketingCampaignVo.getStatus().equals(1)){
                        marketingCampaignVo.setTimeLimitStatus(1);
                    }else {
                        marketingCampaignVo.setTimeLimitStatus(3);
                    }
                }
                if (endTime.before(nowTime)){
                    //已结束
                    marketingCampaignVo.setTimeLimitStatus(3);
                }
            }
        }
        return marketingCampaignVoList;
    }

    @Override
    public GoodInfoVo getGoodInfo(Long id){
        MarketingCampaignVo campaignVo = campaignCilent.getCampaignsByGoodsId(LocalDate.now().toString(), id);
        if(null == campaignVo)
            throw new ConstraintViolationException("当前商品没有设置限时购活动!", new HashSet<>());

        SeckillGoodsVo seckillGoods = campaignVo.getDiscountSeckillInfo().getSeckillGoodsList().stream().filter(seckillGoodsVo -> id.equals(seckillGoodsVo.getGoodsId())).findFirst().orElse(null);
        return new GoodInfoVo(campaignVo.getDiscountSeckillInfo(), seckillGoods);
    }

    @Override
    public void remind(Long id) {
        MarketingCampaignVo campaignVo = campaignCilent.getCampaignsByGoodsId(LocalDate.now().toString(), id);
        if(null == campaignVo)
            throw new ConstraintViolationException("当前商品没有设置限时购活动!", new HashSet<>());

        SeckillGoodsVo seckillGoods = campaignVo.getDiscountSeckillInfo().getSeckillGoodsList().stream().filter(seckillGoodsVo -> id.equals(seckillGoodsVo.getGoodsId())).findFirst().orElse(null);

        Date endSeckillTime = campaignVo.getDiscountSeckillInfo().getEndSeckillTime();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(endSeckillTime.toInstant(), ZoneId.systemDefault());

        if(localDateTime.compareTo(LocalDateTime.now().plusMinutes(5)) < 0)
            throw new ConstraintViolationException("准备好，抢购马上开始咯！", new HashSet<>());

        GoodsTimelimitedVo goodsTimelimitedVo = new GoodsTimelimitedVo();
        goodsTimelimitedVo.setContent("你关注的商品『" + seckillGoods.getGoodsName() + "』马上要开抢啦！");
        Map map = new HashMap();
        map.put("act", "miaosha");
        map.put("id", id);
        goodsTimelimitedVo.setExtras(map);
        goodsTimelimitedVo.setPushtype(1);
        goodsTimelimitedVo.setStartdate(localDateTime.minusMinutes(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        goodsTimelimitedVo.setType("member");
        goodsTimelimitedVo.setUserid(UserUtil.getCurrentUserId());
        goodsTimelimitedVo.setSign(SignatureUtil.signParams(goodsTimelimitedVo));
        Map result = bmsClient.sendMsg(goodsTimelimitedVo);
        log.info("限时购活动推送返回result:{}", result);
    }
}
