package com.jinnjo.sale.service;

import com.jinnjo.sale.clients.CampaignCilent;
import com.jinnjo.sale.domain.TimeLimitBuy;
import com.jinnjo.sale.domain.vo.TimeLimitBuyVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TimeLimitBuyService {
    private final CampaignCilent campaignCilent;

    public TimeLimitBuyService(CampaignCilent campaignCilent){
        this.campaignCilent = campaignCilent;
    }

    public TimeLimitBuy add(TimeLimitBuyVo timeLimitBuyVo){
        campaignCilent.addCampaigns(null);
        return null;
    }
}
