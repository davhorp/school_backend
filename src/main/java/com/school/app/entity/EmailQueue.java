package com.school.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_queue")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recipient;
    private String subject;
    @Column(columnDefinition = "TEXT")
    private String content;
    private int attempts;
    @Enumerated(EnumType.STRING)
    private EmailStatus status; // PENDING, SENT, FAILED
    private String lastError;
    private LocalDateTime lastAttempt;
    private LocalDateTime createdAt;

    public enum EmailStatus { PENDING, SENT, FAILED }

}
