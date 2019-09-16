package com.jinnjo.sale.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface SaleChannels {

    String SALEOUTPUT = "saleoutput";

    /**
     * 短信推送通道
     */
    @Output(SALEOUTPUT)
    SubscribableChannel output();
}
