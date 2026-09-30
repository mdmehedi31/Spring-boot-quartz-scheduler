package com.qs.service;


import com.qs.dto.CampaignDTO;
import com.qs.dto.CampaignEmailDTO;
import com.qs.entity.CampaignEmailEntity;
import com.qs.entity.CampaignEntity;
import com.qs.repository.CampaignEmailRepository;
import com.qs.repository.CampaignRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CampaignEmailService {

    private final CampaignEmailRepository campaignEmailRepository;
    private final CampaignRepository campaignRepository;

    private static final Logger log = LoggerFactory.getLogger(CampaignEmailService.class);

    public CampaignEmailService(CampaignEmailRepository campaignEmailRepository,
                                CampaignRepository campaignRepository) {
        this.campaignEmailRepository = campaignEmailRepository;
        this.campaignRepository = campaignRepository;
    }

// This is just for testing purpose, there need to handle exception and other validation
    public String createCampaignEmail(CampaignDTO campaignDTO) {

        try{
            CampaignEntity campaignEntity= new CampaignEntity();
            campaignEntity.setName(campaignDTO.getName());

            campaignEntity= campaignRepository.save(campaignEntity);


            if(campaignEntity != null && campaignDTO.getEmails() != null && campaignDTO.getEmails().size() > 0) {

                List<CampaignEmailEntity> campaignEmailEntityList= new ArrayList<>();
                Long startTime = System.currentTimeMillis();
                for (CampaignEmailDTO campaignEmailDTO : campaignDTO.getEmails()) {
                    CampaignEmailEntity campaignEmailEntity = new CampaignEmailEntity();
                    campaignEmailEntity.setCampId(campaignEntity.getId());
                    campaignEmailEntity.setEmail(campaignEmailDTO.getEmail());
                    campaignEmailEntity.setName(campaignEmailDTO.getName());
                    campaignEmailEntityList.add(campaignEmailEntity);
                }

                this.campaignEmailRepository.saveAll(campaignEmailEntityList);
                Long endTime = System.currentTimeMillis();
                Long totalTime = endTime - startTime;
                log.info("Total take time: "+ TimeUnit.MILLISECONDS.toSeconds(totalTime));
            }

        }catch (Exception e){
            log.error("An exception thrown : ",e);
            return e.getMessage();
        }
        return null;
    }
}
