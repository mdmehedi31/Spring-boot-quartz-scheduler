package com.qs.scheduler;

import com.qs.dto.Timezone;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SchedulerRunner {

    @Autowired
    private Scheduler scheduler;

    private static final Logger log = LoggerFactory.getLogger(SchedulerRunner.class);
    public void helloWorldScheduler(Integer campaignId) {
        try{
            log.info("Hello World Scheduler starting");
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("campaignId", String.valueOf(campaignId));

            JobDetail jobDetail = JobBuilder.newJob(HelloWorldJobs.class)
                    .withIdentity("testjobs"+campaignId,"testgroup")
                    .storeDurably()
                    .setJobData(jobDataMap)
                    .build();

            LocalDateTime dateTime = LocalDateTime.of(
                    LocalDate.now(),
                    LocalTime.of(21, 02)
            );
            ZoneId zoneId = ZoneId.of("Asia/Dhaka");

            Instant instant = dateTime
                    .atZone(zoneId)
                    .toInstant();

            Trigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail).
                    withIdentity("triggerjobs"+campaignId)
                    .startAt(Date.from(instant)).build();

            log.info("Scheduling process time is :: {}", trigger.getKey());


            scheduler.scheduleJob(jobDetail,trigger);

            log.info("Current time       : {}", LocalDateTime.now());
            log.info("Next fire time     : {}", trigger.getNextFireTime());
            log.info("Trigger state      : {}", scheduler.getTriggerState(trigger.getKey()));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<Timezone> getAllTimezones() {

        Set<String> zoneIds = ZoneId.getAvailableZoneIds();

        log.info("ZoneIds :: {}", zoneIds);
        return new ArrayList<>();
    }

    public List<Timezone> getTimezones() {
        List<Timezone> timeZonesList = ZoneId.getAvailableZoneIds().stream()
                .filter(id -> id.contains("/"))
                .filter(id ->!id.startsWith("SystemV"))
                .sorted()
                .map(id -> {

                    ZoneId zone = ZoneId.of(id);
                    ZonedDateTime now = ZonedDateTime.now(zone);
                    String offset = now.getOffset().getId();
                    if (offset.equals("Z")) {
                        offset = "+00:00";
                    }

                    String group = id.split("/")[0];
                    String countryOrRegion =id.split("/")[1];
                    String formattedString =String.format("%s (UTC %s)", countryOrRegion, offset);

                    Timezone timeZone = new Timezone();
                    timeZone.setGroup(group);
                    timeZone.setTimezone(formattedString);

                    return timeZone;
                })
                .distinct()
                .collect(Collectors.toList());

        Timezone timeZone = new Timezone();
        timeZone.setGroup("UTC");
        timeZone.setTimezone("UTC (UTC +00:00)");

        timeZonesList.add(timeZone);

        return timeZonesList;
    }

}
