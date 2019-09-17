package com.jinnjo.sale.controller;

import com.jinnjo.sale.domain.vo.OrderSubmitVo;
import com.jinnjo.sale.domain.vo.OrderUnifiedVo;
import com.jinnjo.sale.service.TimeLimitBuyOrderService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Api(tags = "限时购商品活动下单")
@Slf4j
@RestController
@RequestMapping("/time-limit/order")
public class TimeLimitBuyOrderController {
    private final TimeLimitBuyOrderService timeLimitBuyOrderService;

    @Autowired
    public TimeLimitBuyOrderController(TimeLimitBuyOrderService timeLimitBuyOrderService){
        this.timeLimitBuyOrderService = timeLimitBuyOrderService;
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<OrderUnifiedVo> addOrder(@Valid @RequestBody OrderSubmitVo orderSubmitVo){
        log.info("限时购商品活动下单orderSubmitVo:{}", orderSubmitVo);
        return Optional.of(timeLimitBuyOrderService.add(orderSubmitVo)).map(ResponseEntity :: ok).orElse(ResponseEntity.notFound().build());
    }
}
