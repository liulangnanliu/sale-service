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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PreAuthorize("hasAuthority('SQR_ADMIN')")
    @ApiOperation(value = "添加限时购活动", notes = "添加限时购活动")
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void addTimeLimitBuy(@Valid @RequestBody @ApiParam(name = "marketingCampaignVo",value = "新增限时购对象")MarketingCampaignVo marketingCampaignVo){
        log.info("添加限时购接收参数marketingCampaignVo:{}", marketingCampaignVo);
        timeLimitBuyService.add(marketingCampaignVo);
    }

    @PreAuthorize("hasAuthority('SQR_ADMIN')")
    @ApiOperation(value = "修改限时购活动", notes = "修改限时购活动")
    @PutMapping(produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateTimeLimitBuy(@Valid @RequestBody @ApiParam(name = "marketingCampaignVo",value = "修改限时购对象")MarketingCampaignVo marketingCampaignVo){
        log.info("修改限时购接收参数marketingCampaignVo:{}", marketingCampaignVo);
        timeLimitBuyService.update(marketingCampaignVo);
    }

    @ApiOperation(value = "根据id查询限时购活动", notes = "根据id查询限时购活动")
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<MarketingCampaignVo> getTimeLimitBuyById(@ApiParam(name = "id", value = "活动id", required = true) @PathVariable Long id){
        log.info("根据id查询限时购活动id:{}", id);
        return Optional.of(timeLimitBuyService.getTimeLimitBuyById(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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

    @PreAuthorize("hasAuthority('SQR_ADMIN')")
    @ApiOperation(value = "删除限时购活动", notes = "删除限时购活动")
    @DeleteMapping
    public void deleteTimeLimitBuy(@ApiParam(name = "id", value = "活动id", required = true) @RequestParam Long id){
        log.info("删除限时购活动id:{}", id);
        timeLimitBuyService.delete(id);
    }

    @PreAuthorize("hasAuthority('SQR_ADMIN')")
    @ApiOperation(value = "删除限时购活动商品", notes = "删除限时购活动商品")
    @DeleteMapping(value = "/goods")
    public void deleteTimeLimitBuyGoods(@ApiParam(name = "id", value = "活动id", required = true) @RequestParam Long id,
                                        @ApiParam(name = "goodsId", value = "商品id", required = true) @RequestParam Long goodsId){
        log.info("删除限时购活动商品id:{} goodsId:{}", id, goodsId);
        timeLimitBuyService.deleteGoods(id, goodsId);
    }

    @PreAuthorize("hasAuthority('SQR_ADMIN')")
    @ApiOperation(value = "变更限时购活动状态", notes = "变更限时购活动状态")
    @PutMapping(value = "/changeStatus", produces = MediaTypes.HAL_JSON_VALUE)
    public void shutTimeLimitBuy(@ApiParam(name = "id", value = "活动id", required = true) @RequestParam Long id,
                                 @ApiParam(name = "status", value = "活动状态(1 开启 2 关闭)", required = true) @RequestParam Integer status){
        log.info("变更限时购活动状态id:{} status:{}", id, status);
        timeLimitBuyService.changeStatus(id, status);
    }


    @ApiOperation(value = "验证秒杀商品在当天的场次中是否重复", notes = "验证秒杀商品在当天的场次中是否重复")
    @GetMapping(value = "/check", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Long> checkSeckillGoods(@ApiParam(name = "date", value = "校验日期", required = true) @RequestParam String date,
                                                  @ApiParam(name = "goodsId", value = "商品id", required = true) @RequestParam Long goodsId,
                                                  @ApiParam(name = "goodsSpecId", value = "商品规格id") @RequestParam(required = false) Long goodsSpecId){
        log.info("验证秒杀商品在当天的场次中是否重复date:{} goodsId:{} goodsSpecId:{}", date, goodsId, goodsSpecId);

        return Optional.of(timeLimitBuyService.checkSeckillGoods(date, goodsId, goodsSpecId)).map(ResponseEntity :: ok).orElse(ResponseEntity.notFound().build());
    }
}
