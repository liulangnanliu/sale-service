package com.jinnjo.sale.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author winner
 * Create_Time 1/12/2019 1:17 PM
 * description:
 */
@Component
@Slf4j
public class TriggerListenerLogMonitor implements TriggerListener {

    private static final ThreadLocal<DateFormat> LOCAL_DATE_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    @Override
    public String getName() {
        return "triggerListenerLogMonitor";
    }

    //do some thing before all register job invoke execute method
    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext jobExecutionContext) {
        JobKey jobKey = trigger.getJobKey();
        Date now = new Date();
        DateFormat dateFormat = LOCAL_DATE_FORMAT.get();
        log.info("JobGroup:{},JobName:{} begin {}",jobKey.getGroup(),jobKey.getName(),dateFormat.format(now));
    }

    // if return true the job will be vetoed
    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext jobExecutionContext) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        JobKey jobKey = trigger.getJobKey();
        Date now = new Date();
        DateFormat dateFormat = LOCAL_DATE_FORMAT.get();
        log.error("JobGroup:{},JobName:{} miss execution on {}",jobKey.getGroup(),jobKey.getName(),dateFormat.format(now));
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, Trigger.CompletedExecutionInstruction completedExecutionInstruction) {
        JobKey jobKey = trigger.getJobKey();
        Date now = new Date();
        DateFormat dateFormat = LOCAL_DATE_FORMAT.get();
        log.info("JobGroup:{},JobName:{} end {}",jobKey.getGroup(),jobKey.getName(),dateFormat.format(now));
    }
}
