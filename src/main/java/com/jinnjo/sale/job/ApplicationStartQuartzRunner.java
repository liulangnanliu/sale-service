package com.jinnjo.sale.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerListener;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author winner
 * Create_Time 10/19/2018 1:24 PM
 * description:
 */
@Component
@Slf4j
public class ApplicationStartQuartzRunner implements ApplicationRunner {

    private SaleJobQuartzScheduler saleJobQuartzScheduler;
    private JobFactory jobFactory;
    private TriggerListener triggerListener;
    @Autowired
    public ApplicationStartQuartzRunner(SaleJobQuartzScheduler saleJobQuartzScheduler, @Qualifier("saleJobFactory") JobFactory jobFactory, @Qualifier("triggerListenerLogMonitor") TriggerListener triggerListener){
        this.saleJobQuartzScheduler = saleJobQuartzScheduler;
        this.jobFactory = jobFactory;
        this.triggerListener = triggerListener;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            Scheduler scheduler = saleJobQuartzScheduler.getScheduler();
            scheduler.setJobFactory(jobFactory);
            scheduler.getListenerManager().addTriggerListener(triggerListener);
            saleJobQuartzScheduler.startAllDefinedJob();
            log.info("quartz task is running");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
