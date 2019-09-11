package com.jinnjo.sale.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;


/**
 * @author bobo
 * @date 2019\9\10 0010 16:11
 * description:
 */
public interface TimeLimitBuyAppService {
    JSONObject getForTop();
    List<JSONObject> getForList();
}
