package com.example.reminder.job;

import com.example.reminder.entity.Notification;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class WordGeneratorJob extends QuartzJobBean implements InterruptableJob {
    private static final Logger logger = LoggerFactory.getLogger(WordGeneratorJob.class);


    private  volatile Thread thisThread;


    public WordGeneratorJob() {
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        thisThread = Thread.currentThread();

        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        int count = (int) jobDataMap.get("counter");
        /*try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
            thisThread.interrupt(); // restore interrupted status
        }*/
        ArrayList<Notification> dictionary = (ArrayList<Notification>) jobDataMap.get("Dictionary");
        logger.info("Word pair is {}",  dictionary.get(count%100));
        jobDataMap.put("counter", ++count);

         //add web socket

    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        if (thisThread != null) {
            thisThread.interrupt();
        }
    }
}