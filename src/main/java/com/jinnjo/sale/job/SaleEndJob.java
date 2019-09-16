package com.jinnjo.sale.job;

import com.jinnjo.sale.service.TimeLimitBuyService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SaleEndJob implements Job {
    private final TimeLimitBuyService timeLimitBuyService;

    public SaleEndJob(TimeLimitBuyService timeLimitBuyService){
        this.timeLimitBuyService = timeLimitBuyService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getMergedJobDataMap();
        log.info("saleEndJob定时任务开始执行 endTime ：" + dataMap.get("endTime"));
        timeLimitBuyService.executeSaleEndJob(dataMap.getString("startTime"), dataMap.getString("endTime"));


    }
}
