package com.jinnjo.sale.controller;

import com.jinnjo.sale.domain.TimeLimitBuy;
import com.jinnjo.sale.service.TimeLimitBuyAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author bobo
 * @date 2019\9\10 0010 14:21
 * description:
 */
@Api(tags = "限时购活动")
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
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<TimeLimitBuy>> getForTop(){
        return Optional.of(timeLimitBuyAppService.getForTop()).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
