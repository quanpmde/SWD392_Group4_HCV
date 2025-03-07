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
    @Column(name = "questionId")
    private int questionBankID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subjectId")
    private Subject subject;

    @OneToMany(mappedBy = "questionBank",cascade = CascadeType.ALL)
    private List<Question> questions;

    @ManyToMany(mappedBy = "questionBanks", cascade = CascadeType.ALL)
    private List<Exam> exams;

    @Column(name = "questionBankName")
    private String questionBankName;

    @Column(name = "questionBankDate")
    private LocalDateTime questionBankDate;

    @Column(name = "questionBankDescription")
    private String questionBankDescription;

    @Column(name = "questionBankStatus")
    private String questionBankStatus;

    @Column(name = "questionBankImage")
    private String questionBankImage;
}
