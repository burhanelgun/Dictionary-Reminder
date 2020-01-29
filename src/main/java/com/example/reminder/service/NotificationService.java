package com.example.reminder.service;

import com.example.reminder.entity.Notification;
import com.example.reminder.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("notificationService")
public class NotificationService {

    @Autowired
    NotificationRepository noteRepository;

    public List<Notification> findAll() {
        return noteRepository.findAll();
    }
}
