package com.ojt_Project.OJT_Project_11_21.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private int questionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_bank_id",nullable = true)
    private QuestionBank questionBank;

    @ManyToMany(mappedBy = "questions",cascade = CascadeType.ALL)
    private List<Exam> exams;

    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
    private List<Answer> answers;

    @ManyToMany(mappedBy = "questions")
    private Collection<Attempt> attempts;

    @Column(name = "question_description")
    private String questionDescription;

    @Column(name = "question_answer_explain")
    private String questionAnswerExplain;

    @Column(name = "question_image")
    private String questionImage;
}
