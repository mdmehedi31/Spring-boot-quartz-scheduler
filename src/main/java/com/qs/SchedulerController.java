package com.qs;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/run")
public class SchedulerController {

    private SchedulerRunner schedulerRunner;

    public SchedulerController(SchedulerRunner schedulerRunner) {
        this.schedulerRunner = schedulerRunner;
    }


    @GetMapping("")
    public void runscheduler(){
        schedulerRunner.helloWorldScheduler();
    }
}
