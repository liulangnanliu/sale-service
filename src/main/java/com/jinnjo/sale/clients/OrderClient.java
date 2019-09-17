package com.jinnjo.sale.clients;

import com.jinnjo.sale.domain.vo.OrderSubmitVo;
import com.jinnjo.sale.domain.vo.OrderUnifiedVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(name = "orderClient",url = "${sale.ods.service.url}")
public interface OrderClient {
    @PostMapping(value = "/orders")
    OrderUnifiedVo orders(@RequestBody OrderSubmitVo orderSubmitVo);


    @PostMapping(value = "/orders-limited-time-purchase")
    OrderUnifiedVo limitTimeOrder(@RequestBody OrderSubmitVo orderSubmitVo);
}
