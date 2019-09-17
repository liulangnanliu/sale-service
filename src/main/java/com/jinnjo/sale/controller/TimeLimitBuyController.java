package com.jinnjo.sale.controller;

import com.jinnjo.sale.domain.vo.MarketingCampaignVo;
import com.jinnjo.sale.service.TimeLimitBuyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Api(tags = "限时购商品活动")
@Slf4j
@RestController
@RequestMapping("/time-limit")
public class TimeLimitBuyController {
    private final TimeLimitBuyService timeLimitBuyService;

    @Autowired
    public TimeLimitBuyController(TimeLimitBuyService timeLimitBuyService){
        this.timeLimitBuyService = timeLimitBuyService;
    }

    @ApiOperation(value = "添加限时购活动", notes = "添加限时购活动")
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void addTimeLimitBuy(@Valid @RequestBody @ApiParam(name = "marketingCampaignVo",value = "新增限时购对象")MarketingCampaignVo marketingCampaignVo){
        log.info("添加限时购接收参数marketingCampaignVo:{}", marketingCampaignVo);
        timeLimitBuyService.add(marketingCampaignVo);
    }


    @ApiOperation(value = "修改限时购活动", notes = "修改限时购活动")
    @PutMapping(produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateTimeLimitBuy(@Valid @RequestBody @ApiParam(name = "marketingCampaignVo",value = "修改限时购对象")MarketingCampaignVo marketingCampaignVo){
        log.info("修改限时购接收参数marketingCampaignVo:{}", marketingCampaignVo);
        timeLimitBuyService.update(marketingCampaignVo);
    }

    @ApiOperation(value = "查询限时购活动", notes = "查询限时购活动")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<PagedResources<Resource<MarketingCampaignVo>>> getTimeLimitBuy(@ApiParam(name = "page", value = "当前页", required = true) @RequestParam Integer page,
                                                                                         @ApiParam(name = "size", value = "每页条数", required = true) @RequestParam Integer size,
                                                                                         @ApiParam(name = "startSeckillTime", value = "活动开始时间") @RequestParam(required = false) String startSeckillTime,
                                                                                         @ApiParam(name = "endSeckillTime", value = "活动结束时间") @RequestParam(required = false) String endSeckillTime,
                                                                                         @ApiParam(name = "status", value = "活动状态(待开启  0  已开启 1   已结束3)") @RequestParam(required = false) Integer status,
                                                                                         PagedResourcesAssembler<MarketingCampaignVo> pagedResourcesAssembler){
        log.info("查询限时购活动参数page:{} size:{} startSeckillTime:{} endSeckillTime:{}", page, size, startSeckillTime, endSeckillTime);
        return Optional.of(timeLimitBuyService.getTimeLimitBuy(page, size , startSeckillTime , endSeckillTime, status))
                       .map(t -> ResponseEntity.ok(pagedResourcesAssembler.toResource(t)))
                       .orElse(ResponseEntity.notFound().build());
    }

}
