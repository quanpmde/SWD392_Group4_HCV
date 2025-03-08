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
    @Column(name = "exam_id")
    private int examId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id",nullable = true)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = true)
    private User user;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "exam_questionBank",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "questionBank_id")
    )
    private Collection<QuestionBank> questionBanks;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "exam_question",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<Question> questions;

    @OneToMany(mappedBy = "exam",cascade = CascadeType.ALL)
    private List<Attempt> attempts;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<Report> reports ;

    @Column(name = "exam_name",  length = 255)
    private String examName;

    @Column(name = "exam_Password", length = 32)
    private String examPassword;

    @Column(name = "exam_total_questions")
    private int examTotalQuestions;

    @Column(name = "exam_start_date")
    private LocalDateTime examStartDate;

    @Column(name = "exam_end_date")
    private LocalDateTime examEndDate;

    @Column(name = "exam_timer")
    private int examTimer;

    @Column(name = "exam_attempt_count")
    private int examAttemptCount;

    @Column(name = "exam_view_count")
    private int examViewCount;

    @Column(name = "exam_like_count")
    private int examLikeCount;

    @Column(name ="exam_comment_count")
    private int examCommentCount;

    @Column(name = "exam_image",length = 1000)
    private String examImage;

    @Column(name = "exam_status",length = 50)
    private String examStatus;


}
