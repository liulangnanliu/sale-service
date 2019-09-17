package com.jinnjo.sale.service;

import com.alibaba.fastjson.JSON;
import com.jinnjo.sale.domain.GoodsSqr;
import com.jinnjo.sale.domain.vo.ListenGoodsView;
import com.jinnjo.sale.repo.GoodsSqrRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author bobo
 * @date 2019\9\17 0017 15:30
 * description:
 */
@Service
@Slf4j
public class GoodsSqrServiceImpl implements GoodsSqrService{
    private final GoodsSqrRepository goodsSqrRepository;
    @Autowired
    public GoodsSqrServiceImpl(GoodsSqrRepository goodsSqrRepository){
        this.goodsSqrRepository = goodsSqrRepository;
    }
    @Override
    public void update(ListenGoodsView listenGoodsView) {
        GoodsSqr goodsSqr = goodsSqrRepository.findById(listenGoodsView.getId()).orElse(null);
        log.info("商品{}变更",JSON.toJSONString(goodsSqr));
        if (null!=goodsSqr){
            goodsSqr.setTitle(listenGoodsView.getTitle());//商品名称
            goodsSqr.setIcon(listenGoodsView.getIcon());//图片
            goodsSqr.setBuyType(listenGoodsView.getBuyType());//购买方式
            goodsSqr.setSkuInfos(listenGoodsView.getSkuInfos());//商品规格
            goodsSqrRepository.save(goodsSqr);
        }
    }
}
