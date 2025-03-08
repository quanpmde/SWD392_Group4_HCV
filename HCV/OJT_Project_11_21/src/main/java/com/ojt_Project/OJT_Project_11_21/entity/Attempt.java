package com.ojt_Project.OJT_Project_11_21.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Attempt")
public class Attempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_id")
    private int attemptId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "attempt_question",
            joinColumns = @JoinColumn(name = "attempt_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private Collection<Question> questions;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "attempt_answer",
            joinColumns = @JoinColumn(name = "attempt_id"),
            inverseJoinColumns = @JoinColumn(name = "answer_id")
    )
    private Collection<Answer> answers;

    @Column(name = "attempt_count")
    private int attemptCount;

    @Column(name = "attempt_start_date")
    private LocalDateTime attemptStartDate;

    @Column(name = "attempt_end_date")
    private LocalDateTime attemptEndDate;

    @Column(name = "attempt_time_submitted")
    private String attemptTimeSubmitted;

    @Column(name = "attempt_point")
    private Double attemptPoint;

    @Column(name = "attempt_correct_answer_count")
    private int attemptCorrectAnswerCount;

    @Column(name = "attempt_status")
    private String attemptStatus;

}
