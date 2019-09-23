package com.jinnjo.sale.clients;

import com.jinnjo.sale.domain.vo.OrderSubmitVo;
import com.jinnjo.sale.domain.vo.OrderUnifiedVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Component
@FeignClient(name = "orderClient",url = "${sale.ods.service.url}")
public interface OrderClient {
    @PostMapping(value = "/orders")
    Map<String, Object> orders(@RequestBody OrderSubmitVo orderSubmitVo);

    @PostMapping(value = "/orders-limited-time-purchase")
    Map<String, Object> limitTimeOrder(@RequestBody OrderSubmitVo orderSubmitVo);

    @PostMapping(value = "/orders/shippingFee")
    Map<String, Object> getShoppingFee(@RequestBody OrderSubmitVo orderSubmitVo, @RequestParam("goodsType") Integer goodsType);
}
