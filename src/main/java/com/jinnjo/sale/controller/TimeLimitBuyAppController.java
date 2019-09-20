package com.jinnjo.sale.controller;

import com.jinnjo.base.annotations.Action;
import com.jinnjo.base.util.UserUtil;
import com.jinnjo.sale.domain.vo.GoodInfoVo;
import com.jinnjo.sale.domain.vo.MarketingCampaignVo;
import com.jinnjo.sale.domain.vo.SeckillGoodsVo;
import com.jinnjo.sale.service.TimeLimitBuyAppService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author bobo
 * @date 2019\9\10 0010 14:21
 * description:
 */
@Api(tags = "限时购活动APP")
@Slf4j
@RestController
@RequestMapping(value = "/time-buy/app")
public class TimeLimitBuyAppController {
    private final TimeLimitBuyAppService timeLimitBuyAppService;
    @Autowired
    public TimeLimitBuyAppController(TimeLimitBuyAppService timeLimitBuyAppService){
        this.timeLimitBuyAppService = timeLimitBuyAppService;
    }

    @ApiOperation(value = "首页限时购", notes = "首页限时购")
    @GetMapping(value = "/top",produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<MarketingCampaignVo> getForTop(@RequestParam("cityCode")String cityCode){
        log.info("首页限时购");
        return Optional.ofNullable(timeLimitBuyAppService.getForTop(cityCode)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "限时购列表", notes = "限时购列表")
    @GetMapping(value = "/list",produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<MarketingCampaignVo>> getForList(@RequestParam("cityCode")String cityCode){
        log.info("限时购列表");
        return Optional.ofNullable(timeLimitBuyAppService.getForList(cityCode)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "限时购详情", notes = "限时购详情")
    @GetMapping(value = "/{id}",produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Page<SeckillGoodsVo>> getById(@PathVariable("id")Long id,@RequestParam("page")Integer page,
                                                        @RequestParam("size")Integer size,@RequestParam("cityCode")String cityCode){
        log.info("限时购详情{}",id);
        return Optional.ofNullable(timeLimitBuyAppService.getById(id,page,size,cityCode)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "限时购商品详情", notes = "限时购商品详情")
    @GetMapping(value = "/goodInfo",produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<GoodInfoVo> getGoodInfo(@ApiParam(name = "id", value = "商品id", required = true) @RequestParam Long id){
        log.info("限时购商品详情id:{}", id);
        return Optional.ofNullable(timeLimitBuyAppService.getGoodInfo(id)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * 限时购开抢提醒
     */
    @PreAuthorize("hasAuthority('SQR_USER')")
    @ApiOperation(value = "限时购活动开抢提醒", notes = "限时购活动开抢提醒")
    @PostMapping(value = "/remind", produces = MediaTypes.HAL_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "商品id" ,required = true, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "activityTime",value = "活动开始时间" ,required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "status",value = "0 设置 -1取消" ,required = true, dataType = "int", paramType = "query")
    })
    public void remind(@RequestParam(name = "id") Long id,@RequestParam("activityTime") String activityTime,@RequestParam("status") Integer status){
        log.info("限时提醒id:{}开始时间{}状态{}", id,activityTime,status);
        timeLimitBuyAppService.remind(id,activityTime,status);
    }

//    @ApiOperation(value = "延迟回调", notes = "延迟回调")
//    @GetMapping(value = "/notify")
//    public void remindNotify(@RequestParam String userId, @RequestParam String goodsId){
//        log.info("延迟回调userId:{} goodsId:{}", userId, goodsId);
//        timeLimitBuyAppService.remindNotify(Long.parseLong(userId), Long.parseLong(goodsId));
//    }
}
