package com.jinnjo.sale.controller;

import com.jinnjo.sale.service.TimeLimitBuyService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "限时购活动")
@Slf4j
@RestController
public class TimeLimitBuyController {
    private final TimeLimitBuyService timeLimitBuyService;

    public TimeLimitBuyController(TimeLimitBuyService timeLimitBuyService){
        this.timeLimitBuyService = timeLimitBuyService;
    }

}
