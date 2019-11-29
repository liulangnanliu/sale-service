package com.jinnjo.sale.clients;

import com.jinnjo.base.feign.FeignSecurityBean;
import com.jinnjo.sale.domain.vo.MarketingCampaignVo;
import com.jinnjo.sale.domain.vo.PageVo;
import com.jinnjo.sale.domain.vo.SeckillGoodsVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Component
@FeignClient(name="campaignClient", url="${sale.mcs.service.url}")
public interface CampaignCilent {

    @RequestMapping(value = "/campaigns",method = RequestMethod.POST, headers = {FeignSecurityBean.SECURITY_AUTH_USER_OR_SERVICE})
    String addCampaigns(@RequestBody MarketingCampaignVo marketingCampaignVo);

    @GetMapping(value = "/campaigns/seckills", headers = {FeignSecurityBean.SECURITY_AUTH_USER_OR_SERVICE})
    List<MarketingCampaignVo> getCampaignsByPage(@RequestParam("date") String date, @RequestParam("applyPlatform") Integer applyPlatform);

    @GetMapping(value = "/campaigns/{id}", headers = {FeignSecurityBean.SECURITY_AUTH_USER_OR_SERVICE})
    MarketingCampaignVo getCampaignById(@PathVariable("id") Long id);

    @PutMapping(value = "/campaigns/{id}", headers = {FeignSecurityBean.SECURITY_AUTH_USER_OR_SERVICE})
    MarketingCampaignVo updateCampaign(@PathVariable("id") String id, @RequestBody MarketingCampaignVo marketingCampaignVo);

    @DeleteMapping(value = "/campaignsbyid/{id}", headers = {FeignSecurityBean.SECURITY_AUTH_USER_OR_SERVICE})
    void deleteCampaign(@PathVariable("id") Long id);

    @DeleteMapping(value = "/campaigns/deletegoods", headers = {FeignSecurityBean.SECURITY_AUTH_USER_OR_SERVICE})
    void deleteGoods(@RequestParam("id") Long id, @RequestParam("goodsId") Long goodsId);

    @PutMapping(value = "/campaigns/change-status/{id}", headers = {FeignSecurityBean.SECURITY_AUTH_USER_OR_SERVICE})
    void changeStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status);

    @GetMapping(value = "/campaigns/seckillgoodsbytime", headers = {FeignSecurityBean.SECURITY_AUTH_USER_OR_SERVICE})
    List<SeckillGoodsVo> getGoodsListBySeckTime(@RequestParam("startSeckillTime") String startSeckillTime, @RequestParam("endSeckillTime") String endSeckillTime, @RequestParam("applyPlatform") Integer applyPlatform);

    @GetMapping(value = "/campaigns/seckill/page", headers = {FeignSecurityBean.SECURITY_AUTH_USER_OR_SERVICE})
    PageVo<MarketingCampaignVo> getSeckillByPage(@RequestParam("startSeckillTime") String startSeckillTime, @RequestParam("endSeckillTime") String endSeckillTime, @RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestParam("status") Integer status, @RequestParam("applyPlatform") Integer applyPlatform);

    @GetMapping(value = "/campaigns/goodsid", headers = {FeignSecurityBean.SECURITY_AUTH_USER_OR_SERVICE})
    List<MarketingCampaignVo> getCampaignsByGoodsId(@RequestParam("date") String date, @RequestParam("goodsId") Long goodsId, @RequestParam("applyPlatform") Integer applyPlatform);

    @GetMapping(value = "/campaigns/checkseckillgoods", headers = {FeignSecurityBean.SECURITY_AUTH_USER_OR_SERVICE})
    Long checkSeckillGoods(@RequestParam("date") String date, @RequestParam("goodsId") Long goodsId, @RequestParam("goodsSpecId") Long goodsSpecId, @RequestParam("applyPlatform") Integer applyPlatform);

    @PostMapping(value = "/campaigns/checkseckilltime", headers = {FeignSecurityBean.SECURITY_AUTH_USER_OR_SERVICE})
    Long checkSeckillTime(@RequestParam("startSeckillTime") String startSeckillTime, @RequestParam("endSeckillTime") String endSeckillTime, @RequestParam("id") String id, @RequestParam("applyPlatform") Integer applyPlatform);

    @GetMapping(value = "/campaigns/checkseckillgoodslist", headers = {FeignSecurityBean.SECURITY_AUTH_USER_OR_SERVICE})
    List<String> checkSeckillGoodsList(@RequestParam("date") String date, @RequestParam("goodsList") String goodsList, @RequestParam("applyPlatform") Integer applyPlatform);

}
