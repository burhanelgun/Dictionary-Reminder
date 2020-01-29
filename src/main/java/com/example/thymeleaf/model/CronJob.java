package com.example.thymeleaf.model;

public class CronJob extends BaseJob{

    String cronExpression;

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }
}
