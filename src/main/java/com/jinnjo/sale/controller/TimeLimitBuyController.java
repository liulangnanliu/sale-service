package com.jinnjo.sale.controller;

import com.jinnjo.sale.domain.TimeLimitBuy;
import com.jinnjo.sale.domain.vo.TimeLimitBuyVo;
import com.jinnjo.sale.service.TimeLimitBuyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import java.util.List;
import java.util.Optional;

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
    public void addTimeLimitBuy(@Valid @RequestBody @ApiParam(name = "timeLimitBuyVo",value = "新增限时购对象")TimeLimitBuyVo timeLimitBuyVo){
        log.info("添加限时购接收参数timeLimitBuyVo{}", timeLimitBuyVo);
        timeLimitBuyService.add(timeLimitBuyVo);
    }

}
