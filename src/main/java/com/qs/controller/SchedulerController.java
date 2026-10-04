package com.qs.controller;


import com.qs.dto.CampaignDTO;
import com.qs.dto.Timezone;
import com.qs.scheduler.SchedulerRunner;
import com.qs.service.CampaignEmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/run")
public class SchedulerController {

    private SchedulerRunner schedulerRunner;

    private CampaignEmailService campaignEmailService;

    public SchedulerController(SchedulerRunner schedulerRunner, CampaignEmailService campaignEmailService) {
        this.schedulerRunner = schedulerRunner;
        this.campaignEmailService = campaignEmailService;
    }


    @GetMapping("/test")
    public void runscheduler() {
        Integer campID1= 10;
        Integer campID2= 20;
        Integer campID3= 30;
        schedulerRunner.helloWorldScheduler(campID1);
        System.out.println("===================================================");
        schedulerRunner.helloWorldScheduler(campID2);
        System.out.println("===================================================");
        schedulerRunner.helloWorldScheduler(campID3);
    }


    @PostMapping("/create")
    public ResponseEntity<String> createCampaign(@RequestBody CampaignDTO campaignDTO){
        String response = campaignEmailService.createCampaignEmail(campaignDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/timezone")
    public ResponseEntity<List<Timezone>> showTimeZone(){
        List<Timezone> zones =  schedulerRunner.getTimezones();
        return new ResponseEntity<>(zones, HttpStatus.OK);
    }
}
