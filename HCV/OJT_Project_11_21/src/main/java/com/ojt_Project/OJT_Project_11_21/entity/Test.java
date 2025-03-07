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
@Table(name = "Test")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "testId")
    private int testId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "examId")
    private Exam exam;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "test_question",
            joinColumns = @JoinColumn(name = "testId"),
            inverseJoinColumns = @JoinColumn(name = "questionId")
    )
    private Collection<Question> questions;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "test_answer",
            joinColumns = @JoinColumn(name = "testId"),
            inverseJoinColumns = @JoinColumn(name = "answerId")
    )
    private Collection<Answer> answers;

    @Column(name = "testAttempt")
    private int testAttempt;

    @Column(name = "testStartDate")
    private LocalDateTime testStartDate;

    @Column(name = "testEndDate")
    private LocalDateTime testEndDate;

    @Column(name = "testTimeSubmitted")
    private String testTimeSubmitted;

    @Column(name = "testPoint")
    private Double testPoint;

    @Column(name = "testCorrectAnswerCount")
    private int testCorrectAnswerCount;

    @Column(name = "testStatus")
    private String testStatus;

}
