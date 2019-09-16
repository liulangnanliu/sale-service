package com.jinnjo.sale.clients;

import com.jinnjo.base.feign.FeignSecurityBean;
import com.jinnjo.sale.domain.vo.MarketingCampaignVo;
import com.jinnjo.sale.domain.vo.PageVo;
import com.jinnjo.sale.domain.vo.SeckillGoodsVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Component
@FeignClient(name="campaignClient", url="${sale.mcs.service.url}")
public interface CampaignCilent {

    @RequestMapping(value = "/campaigns",method = RequestMethod.POST, headers = {FeignSecurityBean.SECURITY_AUTH_SERVICE})
    String addCampaigns(@RequestBody MarketingCampaignVo marketingCampaignVo);

    @GetMapping(value = "/campaigns/seckills", headers = {FeignSecurityBean.SECURITY_AUTH_SERVICE})
    List<MarketingCampaignVo> getCampaignsByPage(@RequestParam("date") String date);

    @GetMapping(value = "/campaigns/{id}", headers = {FeignSecurityBean.SECURITY_AUTH_SERVICE})
    MarketingCampaignVo getCampaignById(@PathVariable("id") String id);

    @PutMapping(value = "/campaigns/{id}", headers = {FeignSecurityBean.SECURITY_AUTH_SERVICE})
    MarketingCampaignVo updateCampaign(@PathVariable("id") String id, @RequestBody MarketingCampaignVo marketingCampaignVo);

    @GetMapping(value = "/campaigns/testgoods", headers = {FeignSecurityBean.SECURITY_AUTH_SERVICE})
    List<SeckillGoodsVo> getGoodsListBySeckTime(@RequestParam("startSeckillTime")String startSeckillTime,@RequestParam("endSeckillTime")String endSeckillTime);

    @GetMapping(value = "/campaigns/seckill/page", headers = {FeignSecurityBean.SECURITY_AUTH_SERVICE})
    PageVo<MarketingCampaignVo> getSeckillByPage(@RequestParam("startSeckillTime")String startSeckillTime, @RequestParam("endSeckillTime")String endSeckillTime, @RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestParam("status") Integer status);

}
