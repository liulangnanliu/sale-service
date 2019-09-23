package com.jinnjo.sale.service;

import com.jinnjo.sale.clients.BmsClient;
import com.jinnjo.sale.clients.CampaignCilent;
import com.jinnjo.sale.clients.GoodsClient;
import com.jinnjo.sale.domain.GoodsSkuSqr;
import com.jinnjo.sale.domain.TimeLimitRemind;
import com.jinnjo.sale.domain.vo.*;
import com.jinnjo.sale.enums.StatusEnum;
import com.jinnjo.sale.repo.TimeLimitRemindRepository;
import com.jinnjo.sale.utils.SignatureUtil;
import com.jinnjo.sale.utils.UserUtil;
import com.jinnjo.sale.domain.vo.DiscountSeckillInfoVo;
import com.jinnjo.sale.domain.vo.GoodInfoVo;
import com.jinnjo.sale.domain.vo.MarketingCampaignVo;
import com.jinnjo.sale.domain.vo.SeckillGoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.text.ParseException;
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
    private final BmsClient bmsClient;
    private final GoodsClient goodsClient;
    private final TimeLimitRemindRepository timeLimitRemindRepository;

    @Autowired
    public TimeLimitBuyAppServiceImpl(CampaignCilent campaignCilent,
                                      GoodsClient goodsClient,
                                      BmsClient bmsClient,
                                      TimeLimitRemindRepository timeLimitRemindRepository){
        this.campaignCilent = campaignCilent;
        this.bmsClient = bmsClient;
        this.goodsClient = goodsClient;
        this.timeLimitRemindRepository = timeLimitRemindRepository;
    }

    @Value("${sale.notify.url}")
    private String notifyUrl;

    @Override
    public MarketingCampaignVo getForTop(String cityCode) {
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
                    seckillGoodsVoList.removeIf(seckillGoodsVo -> null!=seckillGoodsVo.getCitycode()&&!seckillGoodsVo.getCitycode().equals(cityCode)&&!seckillGoodsVo.getCitycode().equals("999999"));
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
                    seckillGoodsVoList.removeIf(seckillGoodsVo -> null!=seckillGoodsVo.getCitycode()&&!seckillGoodsVo.getCitycode().equals(cityCode)&&!seckillGoodsVo.getCitycode().equals("999999"));
                    if (null!=UserUtil.getCurrentUserId()){
                        List<TimeLimitRemind> timeLimitReminds = timeLimitRemindRepository.findByUserIdAndStatus(UserUtil.getCurrentUserId(),StatusEnum.NORMAL.getCode());
                        seckillGoodsVoList.forEach(seckillGoodsVo -> {
                            timeLimitReminds.forEach(timeLimitRemind -> {
                                if (seckillGoodsVo.getGoodsId().equals(timeLimitRemind.getGoodsId())){
                                    seckillGoodsVo.setRemind(1);
                                }
                            });
                        });
                    }
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
                    seckillGoodsVoList.removeIf(seckillGoodsVo -> null!=seckillGoodsVo.getCitycode()&&!seckillGoodsVo.getCitycode().equals(cityCode)&&!seckillGoodsVo.getCitycode().equals("999999"));
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
    public Page<SeckillGoodsVo> getById(Long id,Integer page,Integer size,String cityCode) {
        Pageable pageable = PageRequest.of(page,size);
        MarketingCampaignVo marketingCampaignVo = campaignCilent.getCampaignById(id);
        List<SeckillGoodsVo> seckillGoodsVoList = marketingCampaignVo.getDiscountSeckillInfo().getSeckillGoodsList();
        seckillGoodsVoList.removeIf(seckillGoodsVo -> null!=seckillGoodsVo.getCitycode()&&!seckillGoodsVo.getCitycode().equals(cityCode)&&!seckillGoodsVo.getCitycode().equals("999999"));
        List<String> skuIdList = new ArrayList<>();
        int total = seckillGoodsVoList.size();
        List<SeckillGoodsVo> pageList = seckillGoodsVoList.subList(page*size,total<(page+1)*size?total:(page+1)*size);
        pageList.forEach(seckillGoodsVo -> skuIdList.add(seckillGoodsVo.getGoodsSpecId().toString()));
        String skuId = StringUtils.collectionToDelimitedString(skuIdList,",");
        List<GoodsSkuSqr> goodsSkuSqrList = goodsClient.findGoodsSku(skuId);
        pageList.forEach(seckillGoodsVo -> {
            goodsSkuSqrList.forEach(goodsSkuSqr -> {
                if (seckillGoodsVo.getGoodsSpecId().equals(goodsSkuSqr.getId())){
                    seckillGoodsVo.setStock(goodsSkuSqr.getStock());
                }
            });
        });
        if (null!=UserUtil.getCurrentUserId()){
            List<TimeLimitRemind> timeLimitRemindList = timeLimitRemindRepository.findByUserIdAndStatus(UserUtil.getCurrentUserId(),StatusEnum.NORMAL.getCode());
            pageList.forEach(seckillGoodsVo -> {
                timeLimitRemindList.forEach(timeLimitRemind -> {
                    if (seckillGoodsVo.getGoodsId().equals(timeLimitRemind.getGoodsId())){
                        seckillGoodsVo.setRemind(1);
                    }
                });
            });
        }
        Page<SeckillGoodsVo> seckillGoodsVoPage = new PageImpl(pageList,pageable,total);
        return seckillGoodsVoPage;
    }

    @Override
    public List<MarketingCampaignVo> getForList(String cityCode) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());
        List<MarketingCampaignVo> marketingCampaignVoList = campaignCilent.getCampaignsByPage(date);
        if (marketingCampaignVoList.size()<=0){
            return null;
        }
        for (MarketingCampaignVo marketingCampaignVo:marketingCampaignVoList){
            DiscountSeckillInfoVo discountSeckillInfoVo = marketingCampaignVo.getDiscountSeckillInfo();
            if (null!=discountSeckillInfoVo){
                List<SeckillGoodsVo> seckillGoodsVoList = discountSeckillInfoVo.getSeckillGoodsList();
                seckillGoodsVoList.removeIf(seckillGoodsVo -> null!=seckillGoodsVo.getCitycode()&&!seckillGoodsVo.getCitycode().equals(cityCode)&&!seckillGoodsVo.getCitycode().equals("999999"));
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
        if(null == seckillGoods){
            throw new ConstraintViolationException("当前商品不存在!", new HashSet<>());
        }
        Integer remind = 0;
        if (null!=UserUtil.getCurrentUserId()){
            TimeLimitRemind timeLimitRemind = timeLimitRemindRepository.findByUserIdAndGoodsIdAndStatus(UserUtil.getCurrentUserId(),id,StatusEnum.NORMAL.getCode());
            if (null!=timeLimitRemind){
                remind = 1;
            }
        }
        return new GoodInfoVo(campaignVo.getDiscountSeckillInfo(), seckillGoods,campaignVo.getId(),remind);
    }

    @Override
    public Map<String,Object> remind(Long id,String activityTime,Integer status) {
        Map<String,Object> map = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date time;
        try {
            time = simpleDateFormat.parse(activityTime);
        } catch (ParseException e) {
            log.error("时间转换异常{}",e.getMessage());
            throw new ConstraintViolationException("时间转换异常",new HashSet<>());
        }
        if (status.equals(StatusEnum.NORMAL.getCode())){
            //设置提醒
            Date date = new Date(new Date().getTime()+300000L);
            if (date.after(time)){
                map.put("type",1);
                map.put("message","准备好，抢购马上开始了！");
            }else {
                TimeLimitRemind timeLimitRemind = timeLimitRemindRepository.findByUserIdAndActivityTimeAndGoodsIdAndStatus(UserUtil.getCurrentUserId(),time,id,StatusEnum.DELETE.getCode());
                if (null==timeLimitRemind){
                    TimeLimitRemind newTimeLimitRemind = new TimeLimitRemind();
                    newTimeLimitRemind.setGoodsId(id);
                    newTimeLimitRemind.setUserId(UserUtil.getCurrentUserId());
                    newTimeLimitRemind.setStatus(StatusEnum.NORMAL.getCode());
                    newTimeLimitRemind.setActivityTime(time);
                    timeLimitRemindRepository.save(newTimeLimitRemind);
                }else {
                    timeLimitRemind.setStatus(StatusEnum.NORMAL.getCode());
                    timeLimitRemindRepository.save(timeLimitRemind);
                }
                map.put("type",0);
                map.put("message","已订阅开抢提醒，将在开抢前5分钟进行提醒");
            }
        }else {
            //取消提醒
            TimeLimitRemind timeLimitRemind = timeLimitRemindRepository.findByUserIdAndActivityTimeAndGoodsIdAndStatus(UserUtil.getCurrentUserId(),time,id,StatusEnum.NORMAL.getCode());
            if (null==timeLimitRemind){
                throw new ConstraintViolationException("未找到提醒", new HashSet<>());
            }
            timeLimitRemind.setStatus(StatusEnum.DELETE.getCode());
            timeLimitRemindRepository.save(timeLimitRemind);
            map.put("type",1);
            map.put("message","已取消开抢提醒");
        }
        return map;
//        bmsClient.sendDelayMsg(new OrderMsgText(5000, notifyUrl+"?userId="+UserUtil.getCurrentUserId()+"&goodsId="+id));
    }

    @Override
    public void remindNotify(Long userId, Long id) {

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
        goodsTimelimitedVo.setUserid(userId);
        goodsTimelimitedVo.setSign(SignatureUtil.signParams(goodsTimelimitedVo));
        Map result = bmsClient.sendMsg(goodsTimelimitedVo);
        log.info("限时购活动推送返回result:{}", result);
    }
}
