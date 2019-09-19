package com.jinnjo.sale.service;

import com.jinnjo.sale.domain.vo.GoodInfoVo;
import com.jinnjo.sale.domain.vo.MarketingCampaignVo;
import com.jinnjo.sale.domain.vo.SeckillGoodsVo;
import org.springframework.data.domain.Page;

import java.util.List;


/**
 * @author bobo
 * @date 2019\9\10 0010 16:11
 * description:
 */
public interface TimeLimitBuyAppService {
    MarketingCampaignVo getForTop(String cityCode);
    Page<SeckillGoodsVo> getById(Long id,Integer page,Integer size,String cityCode);
    List<MarketingCampaignVo> getForList(String cityCode);
    GoodInfoVo getGoodInfo(Long id);
}
