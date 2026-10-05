package com.qs.batch;


import com.qs.dto.BatchTestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.infrastructure.item.ItemProcessor;


public class CampaignRecipientStoreProcessor implements ItemProcessor<BatchTestDTO, BatchTestDTO> {


    private Integer campaignId;
    private static final Logger log = LoggerFactory.getLogger(CampaignRecipientStoreProcessor.class);
    public CampaignRecipientStoreProcessor(Integer campaignId) {
        log.info("Constructor :: campaignId : {}", campaignId);
        this.campaignId = campaignId;
    }

    @Override
    public BatchTestDTO process(BatchTestDTO item) throws Exception {
        item.setMessage("Message is processing " + campaignId);
        log.info("Processing item for campaign {}", campaignId);
        return item;
    }
}
