package com.qs.scheduler;

import com.qs.entity.CampaignEmailEntity;
import com.qs.repository.CampaignEmailRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
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


@DisallowConcurrentExecution
@Component
public class HelloWorldJobs extends QuartzJobBean {


    private static final Logger log = LoggerFactory.getLogger(HelloWorldJobs.class);

    private CampaignEmailRepository campaignEmailRepository;


    @Qualifier("asyncJobLauncher")
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

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try{
            log.info("This is from execute internal method");
            String campaignId =context.getMergedJobDataMap().getString("campaignId");
            log.info("Fetch campaignId : " + campaignId);
            Integer campaignIdInt = Integer.parseInt(campaignId);
           /* log.info("campaignId :::: " + campaignIdInt);
            printHelloWorld(campaignIdInt);*/

            JobParameters parameters = new JobParametersBuilder().
                    addString("jobName", "testingJob").
                    addString("campaignId", campaignId.toString()).
                    toJobParameters();

            jobOperator.start(job, parameters);

        }catch (Exception e){
        log.error("An exception thrown : ",e);
        }
    }


}
