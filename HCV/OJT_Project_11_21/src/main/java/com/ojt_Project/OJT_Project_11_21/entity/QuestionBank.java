package com.ojt_Project.OJT_Project_11_21.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "QuestionBank")
public class QuestionBank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private int questionBankID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @OneToMany(mappedBy = "questionBank",cascade = CascadeType.ALL)
    private List<Question> questions;

    @ManyToMany(mappedBy = "questionBanks", cascade = CascadeType.ALL)
    private List<Exam> exams;

    @OneToMany(mappedBy = "questionBank", cascade = CascadeType.ALL)
    private List<Report> reports ;

    @Column(name = "questionBank_name")
    private String questionBankName;

    @Column(name = "questionBank_date")
    private LocalDateTime questionBankDate;

    @Column(name = "questionBank_description")
    private String questionBankDescription;

    @Column(name = "questionBank_status")
    private String questionBankStatus;

    @Column(name = "questionBank_image")
    private String questionBankImage;
}
