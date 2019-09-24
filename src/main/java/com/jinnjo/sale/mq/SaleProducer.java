package com.jinnjo.sale.mq;
import com.jinnjo.sale.domain.vo.GoodsMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Slf4j
@EnableBinding({SaleChannels.class})
public class SaleProducer {

    private final SaleChannels saleChannels;

    @Autowired
    public SaleProducer(SaleChannels saleChannels){
        this.saleChannels = saleChannels;
    }


    public void produceSend(List<GoodsMessage> goodsMessageList, String routingKey){
        log.info("通知商品微服务发送goodsMessageList:{}", goodsMessageList);
        if(null != goodsMessageList && goodsMessageList.size() > 0)
            saleChannels.output().send(MessageBuilder.withPayload(goodsMessageList).setHeader("routing-key",routingKey).build());
    }

}
