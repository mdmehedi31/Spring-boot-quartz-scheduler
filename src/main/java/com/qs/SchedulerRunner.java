package com.qs;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

@Component
public class SchedulerRunner {

    @Autowired
    private Scheduler scheduler;

    private static final Logger log = LoggerFactory.getLogger(SchedulerRunner.class);
    public void helloWorldScheduler() {
        try{
            log.info("Hello World Scheduler starting");
            JobDataMap jobDataMap = new JobDataMap();
            JobDetail jobDetail = JobBuilder.newJob(HelloWorldJobs.class)
                    .withIdentity("testjobs","testgroup")
                    .storeDurably()
                    .setJobData(jobDataMap)
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail).
                    withIdentity("triggerjobs")
                    .withSchedule(
                            CronScheduleBuilder.dailyAtHourAndMinute(10,26)
                                    .inTimeZone(TimeZone.getTimeZone("GMT+6"))).build();

            log.info("Scheduling process time is :: {}", trigger.getKey());


            scheduler.scheduleJob(jobDetail,trigger);


        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
