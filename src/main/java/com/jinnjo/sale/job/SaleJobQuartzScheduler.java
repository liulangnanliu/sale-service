package com.jinnjo.sale.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Map;

/**
 * @author winner
 * Create_Time 10/19/2018 11:28 AM
 * description:
 */
@Component
@Slf4j
public class SaleJobQuartzScheduler {

    private Scheduler scheduler;

    private ApplicationContext applicationContext;

    @Autowired
    public SaleJobQuartzScheduler(Scheduler scheduler, ApplicationContext applicationContext) throws SchedulerException {
        Assert.notNull(scheduler, "scheduler must not be null!");
        this.scheduler = scheduler;
        this.applicationContext = applicationContext;
    }

    public Scheduler getScheduler(){
        return this.scheduler;
    }

    public void startAllDefinedJob() throws SchedulerException {
        scheduler.clear();
        Map<String,Object> jobs = applicationContext.getBeansWithAnnotation(QuartzCronJob.class);
        for (Map.Entry<String, Object> entry : jobs.entrySet()) {
            Object job = entry.getValue();
            @SuppressWarnings("unchecked")
            Class<? extends Job> jobClazz = (Class<? extends Job>) job.getClass();
            if(jobClazz.isAnnotationPresent(QuartzCronJob.class)){
                QuartzCronJob annotation = jobClazz.getAnnotation(QuartzCronJob.class);
                String name = annotation.name();
                String group = annotation.group();
                String cronExpression =annotation.defaultCronExpression();
                startJob(scheduler,jobClazz,name,group,cronExpression);
            }else{
                log.error("job instance：" +jobClazz.getName()+ " not exist annotation QuartzCronJob！");
            }
        }
        scheduler.start();
    }


    public String getJobInfo(String name, String group) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        return String.format("time:%s,state:%s", cronTrigger.getCronExpression(),
                scheduler.getTriggerState(triggerKey).name());
    }


    public boolean modifyJob(String name, String group, String time) throws SchedulerException {
        Date date = null;
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        String oldTime = cronTrigger.getCronExpression();
        if (!oldTime.equalsIgnoreCase(time)) {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
                    .withSchedule(cronScheduleBuilder).build();
            date = scheduler.rescheduleJob(triggerKey, trigger);
        }
        return date != null;
    }

    public void pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
    }


    public void pauseJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.pauseJob(jobKey);
    }

    public void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }

    public void resumeJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.resumeJob(jobKey);
    }

    public void deleteJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.deleteJob(jobKey);
    }

    private void startJob(Scheduler scheduler,Class<? extends Job> clazz,String name,String group,String cronExpression) throws SchedulerException {
        //todo eliminate storeDurably because can not set overwriteExistingJobs
        JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(name,group).build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(name,group)
                .withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    public void startDelayJob(Class<? extends Job> clazz) throws SchedulerException {
        QuartzDelayJob annotation = clazz.getAnnotation(QuartzDelayJob.class);
        if(null==annotation){
            throw new RuntimeException("please use QuartzDelayJob annotation on your delayJob class");
        }
        String name = annotation.name();
        String group = annotation.group();
        Long delayTime = annotation.delayTime();
        JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(name,group).build();
        Trigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail).startAt(new Date(System.currentTimeMillis()+ delayTime)).build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

}
