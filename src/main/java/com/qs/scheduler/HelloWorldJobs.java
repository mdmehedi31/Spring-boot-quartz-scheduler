package com.qs.scheduler;

import com.qs.entity.CampaignEmailEntity;
import com.qs.repository.CampaignEmailRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.TaskExecutorJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;
import java.util.UUID;


@DisallowConcurrentExecution
@Component
public class HelloWorldJobs extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(HelloWorldJobs.class);

    private CampaignEmailRepository campaignEmailRepository;

    private JobOperator jobOperator;
    private Job job;
    public HelloWorldJobs(CampaignEmailRepository campaignEmailRepository,
                          Job job, @Qualifier("asyncJobLauncher") JobOperator jobOperator) {
        this.campaignEmailRepository = campaignEmailRepository;
        this.job = job;
        this.jobOperator = jobOperator;
    }

    private void printHelloWorld(Integer campaignId) {
        log.info("Hey, How are you? \n this is hello world method");

        log.info("<< ::: .....------------------------------- ::: >>>");
        if (campaignId != null) {
            List<CampaignEmailEntity>  emailList = campaignEmailRepository.findAllByCampId(campaignId);
            log.info("We are successfully fetch data from the DB... ::: ... >>> <<<<");
            if (emailList != null && emailList.size() > 0) {
                log.info("Data fetch from DB, size is : " + emailList.size());
            }
        }
    }

   /* @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try{
            log.info("This is from execute internal method");
            String campaignId =context.getMergedJobDataMap().getString("campaignId");
            log.info("Fetch campaignId : " + campaignId);
            Integer campaignIdInt = Integer.parseInt(campaignId);
           *//* log.info("campaignId :::: " + campaignIdInt);
            printHelloWorld(campaignIdInt);*//*

            JobParameters parameters = new JobParametersBuilder().
                    addString("jobName", "testingJob").
                    addString("campaignId", campaignId.toString()).
                    toJobParameters();

            jobOperator.start(job, parameters);

        }catch (Exception e){
        log.error("An exception thrown : ",e);
        }
    }*/

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getMergedJobDataMap();
        String campaignId = dataMap.getString("campaignId");

        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("campaignId", campaignId)
                    .addLong("scheduledFireTime", context.getScheduledFireTime().getTime())
                    .addString("runId", UUID.randomUUID().toString()) // Keeps parameter signature unique per execution
                    .toJobParameters();

            log.info("BATCH LAUNCH | campaignId={} | params={}", campaignId, params);

            jobOperator.start(job, params);

        } catch (Exception e) {
            log.error("Failed to start batch job for campaignId={}", campaignId, e);
            throw new JobExecutionException(e, false);
        }
    }

   /* @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getMergedJobDataMap();
        String campaignId = dataMap.getString("campaignId");
        log.info("Quartz fired: trigger={}, fireInstanceId={}, campaignId={}",
                context.getTrigger().getKey(), context.getFireInstanceId(), campaignId);
        try {

            log.info(
                    "QUARTZ EXECUTION | jobKey={} | triggerKey={} | fireInstanceId={} | campaignId={} | dataMap={}",
                    context.getJobDetail().getKey(),
                    context.getTrigger().getKey(),
                    context.getFireInstanceId(),
                    campaignId,
                    dataMap
            );

            JobParameters params = new JobParametersBuilder()
                    .addString("campaignId", campaignId)
                    .addLong("scheduledFireTime", context.getScheduledFireTime().getTime())
                    .toJobParameters();
            log.info("::::: Executing job : {}, params = {}", job.getName(), params);

            log.info(
                    "BATCH LAUNCH | campaignId={} | params={}",
                    campaignId,
                    params
            );

            //jobOperator.start(job, params);
            // DON'T create 'new' Job objects.
// DO pass job.getName() and parameter string:
            String paramString = "campaignId=" + campaignId +
                    ",scheduledFireTime=" + context.getScheduledFireTime().getTime() +
                    ",runId=" + UUID.randomUUID();

            jobOperator.start(job.getName(), paramString);
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException e) {
            log.warn("Campaign {} already running/completed, skipping: {}", campaignId, e.getMessage());
        } catch (Exception e) {
            throw new JobExecutionException(e, false);   // don't swallow
        }
    }*/

}
