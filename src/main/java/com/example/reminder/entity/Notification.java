package com.example.reminder.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "dictionary")
@EntityListeners(AuditingEntityListener.class)
public class Notification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String turkish;

    @NotBlank
    private String english;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String eng) {
        this.english = english;
    }

    public String getTurkish() {
        return turkish;
    }

    public void setTurkish(String tr) {
        this.turkish = turkish;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", turkish='" + turkish + '\'' +
                ", english='" + english + '\'' +
                '}';
    }

}