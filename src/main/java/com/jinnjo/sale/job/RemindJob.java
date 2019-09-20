package com.jinnjo.sale.job;

import com.jinnjo.sale.service.TimeLimitRemindService;
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
@QuartzCronJob(name = "remindJob",group = "remindGroup",defaultCronExpression = "0 0/1 * * * ?")// 0 0/1 * * * ?    0 0 1 * * ?
public class RemindJob implements Job{
    private final TimeLimitRemindService timeLimitRemindService;

    @Autowired
    public RemindJob(TimeLimitRemindService timeLimitRemindService){
        this.timeLimitRemindService = timeLimitRemindService;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("remindJob定时任务开始执行");
        timeLimitRemindService.job();
        log.info("remindJob定时任务执行结束");
    }
}
