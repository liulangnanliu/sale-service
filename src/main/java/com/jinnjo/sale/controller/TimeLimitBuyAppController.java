package com.jinnjo.sale.controller;

import com.alibaba.fastjson.JSONObject;
import com.jinnjo.sale.service.TimeLimitBuyAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<JSONObject> getForTop(){
        return Optional.of(timeLimitBuyAppService.getForTop()).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "限时购列表", notes = "限时购列表")
    @GetMapping(value = "/list",produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<JSONObject>> getForList(){
        return Optional.of(timeLimitBuyAppService.getForList()).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
