package com.jinnjo.sale.service;

import com.jinnjo.sale.domain.vo.GoodInfoVo;
import com.jinnjo.sale.domain.vo.MarketingCampaignVo;
import com.jinnjo.sale.domain.vo.SeckillGoodsVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;


/**
 * @author bobo
 * @date 2019\9\10 0010 16:11
 * description:
 */
public interface TimeLimitBuyAppService {
    MarketingCampaignVo getForTop();
    Page<SeckillGoodsVo> getById(Long id,Integer page,Integer size);
    List<MarketingCampaignVo> getForList();
    GoodInfoVo getGoodInfo(Long id);
    void remind(Long id);
}
