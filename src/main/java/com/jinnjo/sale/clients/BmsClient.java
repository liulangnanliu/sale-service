package com.jinnjo.sale.clients;

import com.jinnjo.sale.domain.vo.GoodsTimelimitedVo;
import com.jinnjo.sale.domain.vo.OrderMsgText;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Component
@FeignClient(name="BmsClient", url="${sale.bms.service.url}")
public interface BmsClient {

    @PostMapping(value="/jpushmsg")
    Map sendMsg(@RequestBody GoodsTimelimitedVo goodsTimelimitedVo);

    @PostMapping(value = "/depaymsg")
    Map sendDelayMsg(@RequestBody OrderMsgText orderMsgText);
}
