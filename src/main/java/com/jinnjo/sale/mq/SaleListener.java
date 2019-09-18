package com.jinnjo.sale.mq;

import com.alibaba.fastjson.JSON;
import com.jinnjo.sale.domain.vo.ListenGoodsView;
import com.jinnjo.sale.service.GoodsSqrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;


@EnableBinding(SaleChannels.class)
@Slf4j
public class SaleListener {
    private final GoodsSqrService goodsSqrService;

    @Autowired
    public SaleListener(GoodsSqrService goodsSqrService){
        this.goodsSqrService = goodsSqrService;
    }

    @StreamListener(value = SaleChannels.GOODSINPUT)
    public void goodsInfoChange(@Payload ListenGoodsView listenGoodsView){
        log.info("监听商品变更{}", JSON.toJSONString(listenGoodsView));
        goodsSqrService.update(listenGoodsView);
    }

}


