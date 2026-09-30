package com.qs.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;


@Component
public class HelloWorldJobs extends QuartzJobBean {


    private static final Logger log = LoggerFactory.getLogger(HelloWorldJobs.class);


    private void printHelloWorld() {
        log.info("Hey, How are you? \n this is hello world method");
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try{
            log.info("This is from execute internal method");
            printHelloWorld();
        }catch (Exception e){
        log.error("An exception thrown : ",e);
        }
    }
}
