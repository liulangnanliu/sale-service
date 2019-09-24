package com.jinnjo.sale.clients;

import com.jinnjo.sale.domain.GoodsSkuSqr;
import com.jinnjo.sale.domain.GoodsSqr;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Component
@FeignClient(name = "goodsClient",url = "${sale.gds.service.url}")
public interface GoodsClient {
    /**
     * 通过动态条件filter获取商品信息
     * @param filter 动态条件
     * @return 分页的商品信息
     */
    @GetMapping(value = "/goods-sqrs")
    PagedResources<GoodsSqr> findGoods(@RequestParam("size") Integer size, @RequestParam(name = "filter", required = false) String filter);

    @GetMapping(value = "/skus-goods-sku")
    List<GoodsSkuSqr> findGoodsSku(@RequestParam("skuId")String skuId);

    @GetMapping(value = "/skus/{sku-id}")
    GoodsSkuSqr findGoodsSkuById(@PathVariable("sku-id")Long skuId);
}
