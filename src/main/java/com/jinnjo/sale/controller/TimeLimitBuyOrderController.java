package com.jinnjo.sale.controller;

import com.jinnjo.sale.domain.vo.OrderSubmitVo;
import com.jinnjo.sale.domain.vo.OrderUnifiedVo;
import com.jinnjo.sale.service.TimeLimitBuyOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@Api(tags = "限时购商品活动下单")
@Slf4j
@RestController
public class TimeLimitBuyOrderController {
    private final TimeLimitBuyOrderService timeLimitBuyOrderService;

    @Autowired
    public TimeLimitBuyOrderController(TimeLimitBuyOrderService timeLimitBuyOrderService){
        this.timeLimitBuyOrderService = timeLimitBuyOrderService;
    }

    @PostMapping(value = "/time-limit/order", produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasAuthority('SQR_USER')")
    @ApiOperation(value = "限时购商品下单", notes = "限时购商品下单")
    public ResponseEntity<Map<String, Object>> addOrder(@Valid @RequestBody OrderSubmitVo orderSubmitVo){
        log.info("限时购商品活动下单orderSubmitVo:{}", orderSubmitVo);
        return Optional.of(timeLimitBuyOrderService.add(orderSubmitVo)).map(ResponseEntity :: ok).orElse(ResponseEntity.notFound().build());
    }


    @PostMapping(value = "/time-limit/shoppingFee", produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasAuthority('SQR_USER')")
    @ApiOperation(value = "邮费查询", notes = "邮费查询")
    public ResponseEntity<Map<String, Object>> getShoppingFee(@Valid @RequestBody OrderSubmitVo orderSubmitVo){
        log.info("邮费查询orderSubmitVo:{}", orderSubmitVo);
        return Optional.of(timeLimitBuyOrderService.getShoppingFee(orderSubmitVo)).map(ResponseEntity :: ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/time-limit/goods-limit")
    @PreAuthorize("hasAuthority('SQR_USER')")
    @ApiOperation(value = "限购查询", notes = "限购查询")
    public void goodsLimit(@ApiParam(name = "goodsId", value = "商品id", required = true) @RequestParam Long goodsId,
                           @ApiParam(name = "skuId", value = "商品规格id", required = true) @RequestParam Long skuId,
                           @ApiParam(name = "count", value = "购买数量", required = true) @RequestParam Integer count){
        log.info("限购查询goodsId:{} skuId:{} count:{}", goodsId, skuId, count);
        timeLimitBuyOrderService.goodsLimit(goodsId, skuId, count);
    }
}
