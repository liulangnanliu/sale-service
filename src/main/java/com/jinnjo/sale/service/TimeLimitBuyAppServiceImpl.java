package com.jinnjo.sale.service;

import com.alibaba.fastjson.JSONObject;
import com.jinnjo.sale.clients.CampaignCilent;
import com.jinnjo.sale.domain.TimeLimitBuy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author bobo
 * @date 2019\9\10 0010 16:11
 * description:
 */
@Service
public class TimeLimitBuyAppServiceImpl implements TimeLimitBuyAppService{
    private final CampaignCilent campaignCilent;
    @Autowired
    public TimeLimitBuyAppServiceImpl(CampaignCilent campaignCilent){
        this.campaignCilent = campaignCilent;
    }


    @Override
    public List<TimeLimitBuy> getForTop() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());
        JSONObject jsonObject = JSONObject.parseObject(campaignCilent.getCampaignsByPage(date));
        List<TimeLimitBuy> timeLimitBuyList = new ArrayList<>();
        return timeLimitBuyList;
    }
}
