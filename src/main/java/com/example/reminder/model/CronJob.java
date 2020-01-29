package com.example.reminder.model;

public class CronJob extends com.example.reminder.model.BaseJob {

    String cronExpression;

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }
}
