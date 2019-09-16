package com.jinnjo.sale.controller;

import com.jinnjo.sale.domain.vo.MarketingCampaignVo;
import com.jinnjo.sale.service.TimeLimitBuyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "限时购商品活动")
@Slf4j
@RestController
@RequestMapping("time-limit")
public class TimeLimitBuyController {
    private final TimeLimitBuyService timeLimitBuyService;

    @Autowired
    public TimeLimitBuyController(TimeLimitBuyService timeLimitBuyService){
        this.timeLimitBuyService = timeLimitBuyService;
    }

    @ApiOperation(value = "添加限时购", notes = "添加限时购")
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void addTimeLimitBuy(@Valid @RequestBody @ApiParam(name = "marketingCampaignVo",value = "新增限时购对象")MarketingCampaignVo marketingCampaignVo){
        log.info("添加限时购接收参数marketingCampaignVo{}", marketingCampaignVo);
        timeLimitBuyService.add(marketingCampaignVo);
    }


    @ApiOperation(value = "修改限时购", notes = "修改限时购")
    @PutMapping(produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateTimeLimitBuy(@Valid @RequestBody @ApiParam(name = "marketingCampaignVo",value = "修改限时购对象")MarketingCampaignVo marketingCampaignVo){
        log.info("修改限时购接收参数marketingCampaignVo{}", marketingCampaignVo);
        timeLimitBuyService.update(marketingCampaignVo);
    }
}
