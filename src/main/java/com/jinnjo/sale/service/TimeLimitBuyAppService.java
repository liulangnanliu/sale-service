package com.jinnjo.sale.service;

import com.jinnjo.sale.domain.vo.MarketingCampaignVo;

import java.util.List;


/**
 * @author bobo
 * @date 2019\9\10 0010 16:11
 * description:
 */
public interface TimeLimitBuyAppService {
    MarketingCampaignVo getForTop();
    List<MarketingCampaignVo> getForList();
}
