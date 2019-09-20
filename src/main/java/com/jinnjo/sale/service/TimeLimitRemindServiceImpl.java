package com.jinnjo.sale.service;

import com.jinnjo.sale.domain.TimeLimitRemind;
import com.jinnjo.sale.enums.StatusEnum;
import com.jinnjo.sale.repo.TimeLimitRemindRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author bobo
 * @date 2019\9\20 0020 10:33
 * description:
 */
@Service
public class TimeLimitRemindServiceImpl implements TimeLimitRemindService{
    private final TimeLimitRemindRepository timeLimitRemindRepository;
    private final TimeLimitBuyAppService timeLimitBuyAppService;
    @Autowired
    public TimeLimitRemindServiceImpl(TimeLimitRemindRepository timeLimitRemindRepository,TimeLimitBuyAppService timeLimitBuyAppService){
        this.timeLimitRemindRepository = timeLimitRemindRepository;
        this.timeLimitBuyAppService = timeLimitBuyAppService;
    }
    @Override
    public void job() {
        Date date = new Date(new Date().getTime()+300000L);
        List<TimeLimitRemind> timeLimitRemindList = timeLimitRemindRepository.findByStatusAndActivityTimeLessThanEqual(StatusEnum.NORMAL.getCode(),date);
        timeLimitRemindList.forEach(timeLimitRemind ->{
            timeLimitBuyAppService.remindNotify(timeLimitRemind.getUserId(),timeLimitRemind.getGoodsId());
            timeLimitRemind.setStatus(StatusEnum.DELETE.getCode());
        });
        timeLimitRemindRepository.saveAll(timeLimitRemindList);
    }
}
