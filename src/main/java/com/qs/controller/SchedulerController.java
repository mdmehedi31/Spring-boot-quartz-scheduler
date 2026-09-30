package com.qs.controller;


import com.qs.dto.CampaignDTO;
import com.qs.scheduler.SchedulerRunner;
import com.qs.service.CampaignEmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/run")
public class SchedulerController {

    private SchedulerRunner schedulerRunner;

    private CampaignEmailService campaignEmailService;

    public SchedulerController(SchedulerRunner schedulerRunner) {
        this.schedulerRunner = schedulerRunner;
    }


    @GetMapping("")
    public void runscheduler(){
        schedulerRunner.helloWorldScheduler();
    }


    @PostMapping("/create")
    public ResponseEntity<String> createCampaign(@RequestBody CampaignDTO campaignDTO){
        String response = campaignEmailService.createCampaignEmail(campaignDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
