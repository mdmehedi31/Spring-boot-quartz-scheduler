package com.qs.scheduler;

import com.qs.entity.CampaignEmailEntity;
import com.qs.repository.CampaignEmailRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class HelloWorldJobs extends QuartzJobBean {


    private static final Logger log = LoggerFactory.getLogger(HelloWorldJobs.class);

    private CampaignEmailRepository campaignEmailRepository;

    public HelloWorldJobs(CampaignEmailRepository campaignEmailRepository) {
        this.campaignEmailRepository = campaignEmailRepository;
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
            log.info("campaignId :::: " + campaignIdInt);
            printHelloWorld(campaignIdInt);
        }catch (Exception e){
        log.error("An exception thrown : ",e);
        }
    }
}
