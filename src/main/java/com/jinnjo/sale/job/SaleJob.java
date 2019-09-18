package com.jinnjo.sale.job;

import com.jinnjo.sale.service.TimeLimitBuyService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@DisallowConcurrentExecution
@Component
@Slf4j
@QuartzCronJob(name = "saleJob",group = "saleGroup",defaultCronExpression = "0 0 1 1 * ?")// 0 0/1 * * * ?    0 0 1 1 * ?
public class SaleJob implements Job{
    private final TimeLimitBuyService timeLimitBuyService;

    @Autowired
    public SaleJob(TimeLimitBuyService timeLimitBuyService){
        this.timeLimitBuyService = timeLimitBuyService;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("saleJob定时任务开始执行");
        timeLimitBuyService.executeJob();
        log.info("saleJob定时任务执行结束");
    }
}
