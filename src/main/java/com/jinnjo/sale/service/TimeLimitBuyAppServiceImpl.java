package com.jinnjo.sale.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jinnjo.sale.clients.CampaignCilent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    @Autowired
    public TimeLimitBuyAppServiceImpl(CampaignCilent campaignCilent){
        this.campaignCilent = campaignCilent;
    }


    @Override
    public JSONObject getForTop() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = simpleDateFormat.format(new Date());
        JSONArray jsonArray = JSONArray.parseArray(campaignCilent.getCampaignsByPage(date));
        if (jsonArray.size()<=0){
            return new JSONObject();
        }
        List<JSONObject> jsonObjects = new ArrayList<>();
        for (Object object:jsonArray){
            JSONObject jsonObject = JSONObject.parseObject(object.toString());
            JSONObject discountSeckillInfo = jsonObject.getJSONObject("discountSeckillInfo");
            String startSeckillTime = discountSeckillInfo.getString("startSeckillTime");
            String endSeckillTime = discountSeckillInfo.getString("endSeckillTime");
            Date startTime;
            Date endTime;
            Date nowTime = new Date();
            try {
                startTime = dateFormat.parse(startSeckillTime);
                endTime = dateFormat.parse(endSeckillTime);
            } catch (ParseException e) {
                log.error("时间转换异常{}",e.getMessage());
                throw new ConstraintViolationException("时间转换异常",new HashSet<>());
            }
            if (startTime.before(nowTime)&&endTime.after(nowTime)){
                //当前时间在活动时间段内
                return jsonObject;
            }
            if (endTime.after(nowTime)){
                Long start = startTime.getTime();
                Long now = nowTime.getTime();
                Long interval = start - now;
                jsonObject.put("interval",interval);
                jsonObjects.add(jsonObject);
            }
        }
        return jsonObjects.stream().min(Comparator.comparing(item -> item.getLong("interval"))).orElse(null);
    }

    @Override
    public List<Object> getForList() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());
        return JSONArray.parseArray(campaignCilent.getCampaignsByPage("2019-09-10"));
    }
}
