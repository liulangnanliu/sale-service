package com.jinnjo.sale.controller;

import com.jinnjo.sale.domain.vo.GoodInfoVo;
import com.jinnjo.sale.domain.vo.MarketingCampaignVo;
import com.jinnjo.sale.domain.vo.SeckillGoodsVo;
import com.jinnjo.sale.service.TimeLimitBuyAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author bobo
 * @date 2019\9\10 0010 14:21
 * description:
 */
@Api(tags = "限时购活动APP")
@Slf4j
@RestController
@RequestMapping(value = "/time-buy/app")
public class TimeLimitBuyAppController {
    private final TimeLimitBuyAppService timeLimitBuyAppService;
    @Autowired
    public TimeLimitBuyAppController(TimeLimitBuyAppService timeLimitBuyAppService){
        this.timeLimitBuyAppService = timeLimitBuyAppService;
    }

    @ApiOperation(value = "首页限时购", notes = "首页限时购")
    @GetMapping(value = "/top",produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<MarketingCampaignVo> getForTop(@RequestParam("cityCode")String cityCode){
        log.info("首页限时购");
        return Optional.ofNullable(timeLimitBuyAppService.getForTop(cityCode)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "限时购列表", notes = "限时购列表")
    @GetMapping(value = "/list",produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<MarketingCampaignVo>> getForList(@RequestParam("cityCode")String cityCode){
        log.info("限时购列表");
        return Optional.ofNullable(timeLimitBuyAppService.getForList(cityCode)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "限时购详情", notes = "限时购详情")
    @GetMapping(value = "/{id}",produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Page<SeckillGoodsVo>> getById(@PathVariable("id")Long id,@RequestParam("page")Integer page,
                                                        @RequestParam("size")Integer size,@RequestParam("cityCode")String cityCode){
        log.info("限时购详情{}",id);
        return Optional.ofNullable(timeLimitBuyAppService.getById(id,page,size,cityCode)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "限时购商品详情", notes = "限时购商品详情")
    @GetMapping(value = "/goodInfo",produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<GoodInfoVo> getGoodInfo(@ApiParam(name = "id", value = "商品id", required = true) @RequestParam Long id){
        log.info("限时购商品详情id:{}", id);
        return Optional.ofNullable(timeLimitBuyAppService.getGoodInfo(id)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
