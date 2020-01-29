package com.example.thymeleaf.controller;

import com.example.thymeleaf.model.CronJob;
import com.example.thymeleaf.model.SimpleJob;
import com.example.thymeleaf.job.WordGeneratorJob;
import com.example.thymeleaf.service.NotificationService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.quartz.CronScheduleBuilder.cronSchedule;

@Controller
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private Scheduler scheduler;


    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("simpleJob",new SimpleJob());

        return "simple";
    }

    @RequestMapping("/createSimpleJob")
    public String CreateSimpleJob(Model model) {
        //sor
        //model.addAttribute("simpleJob",new SimpleJob());

        return "simple";
    }

    @RequestMapping("/createCronJob")
    public String CreateCronJob(Model model) {
        model.addAttribute("cronJob",new CronJob());

        return "cron";
    }


    @PostMapping("/startNotificationCron")
    public String startNotificationCron(@ModelAttribute CronJob cronJob, Model model) {

        try {
            model.addAttribute("cronJob",cronJob);

            logger.info("Cron Job may be start");

            JobDetail jobDetail = buildJobDetail(cronJob.getJobName(),cronJob.getJobGroup());
            Trigger trigger = buildCronJobTrigger(jobDetail,cronJob.getTriggerName(),cronJob.getTriggerGroup(),cronJob.getCronExpression());
            if(scheduler.isShutdown()){
                scheduler.start();
            }
            scheduler.scheduleJob(jobDetail, trigger);

            logger.info("Cron Job is started.");
            return "cron";
        } catch (SchedulerException ex) {
            logger.error("Error scheduling notification", ex);
            return "error-problem";
        }
    }
    private Trigger buildCronJobTrigger(JobDetail jobDetail, String triggerName, String triggerGroup,String cronExpression) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(triggerName, triggerGroup)
                .withSchedule(cronSchedule(cronExpression))
                .forJob(jobDetail)
                .build();
    }

    @PostMapping("/startNotificationSimple")
    public String startNotificationSimple(@ModelAttribute SimpleJob simpleJob, Model model) {
        try {

            model.addAttribute("simpleJob",simpleJob);
            logger.info("SimpleSchedule Job may be start");

            JobDetail jobDetail = buildJobDetail(simpleJob.getJobName(),simpleJob.getJobGroup());
            Trigger trigger = buildJobSimpleTrigger(jobDetail,simpleJob.getTriggerName(),simpleJob.getTriggerGroup(),simpleJob.getInterval());
            if(scheduler.isShutdown()){
                scheduler.start();
            }
            scheduler.scheduleJob(jobDetail, trigger);

            logger.info("SimpleSchedule Job is started.");
            return "simple";
        } catch (SchedulerException ex) {
            logger.error("Error scheduling notification", ex);
            return "error-problem";
        }
    }

    private Trigger buildJobSimpleTrigger(JobDetail jobDetail,String triggerName,String triggerGroup,String interval) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(triggerName, triggerGroup)
                .withDescription("Get new job pair trigger")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(Integer.parseInt(interval)))
                .build();
    }

    private JobDetail buildJobDetail(String jobName,String jobGroup) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("Dictionary", notificationService.findAll());
        jobDataMap.put("counter", 0);

        return JobBuilder.newJob(WordGeneratorJob.class)
                .withIdentity(jobName, jobGroup)
                .withDescription("Send Email Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

}