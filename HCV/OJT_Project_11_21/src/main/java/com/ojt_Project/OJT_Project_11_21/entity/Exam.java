package com.ojt_Project.OJT_Project_11_21.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Exam")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "examId")
    private int examId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subjectId",nullable = true)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",nullable = true)
    private User user;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "exam_questionBank",
            joinColumns = @JoinColumn(name = "examId"),
            inverseJoinColumns = @JoinColumn(name = "questionBankId")
    )
    private Collection<QuestionBank> questionBanks;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "exam_question",
            joinColumns = @JoinColumn(name = "examId"),
            inverseJoinColumns = @JoinColumn(name = "questionId")
    )
    private List<Question> questions;

    @OneToMany(mappedBy = "exam",cascade = CascadeType.ALL)
    private List<Test> tests;

    @Column(name = "examName",  length = 255)
    private String examName;

    @Column(name = "examPassword", length = 32)
    private String examPassword;

    @Column(name = "examTotalQuestions")
    private int examTotalQuestions;

    @Column(name = "examStartDate")
    private LocalDateTime examStartDate;

    @Column(name = "examEndDate")
    private LocalDateTime examEndDate;

    @Column(name = "examTimer")
    private int examTimer;

    @Column(name = "examAttempt")
    private int examAttempt;

    @Column(name = "examViewCount")
    private int examViewCount;

    @Column(name = "examLikeCount")
    private int examLikeCount;

    @Column(name ="examCommentCount")
    private int examCommentCount;

    @Column(name = "examImage",length = 1000)
    private String examImage;

    @Column(name = "examStatus",length = 50)
    private String examStatus;


}
