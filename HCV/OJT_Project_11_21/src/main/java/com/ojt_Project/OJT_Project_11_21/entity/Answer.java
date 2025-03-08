package com.ojt_Project.OJT_Project_11_21.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Answer")
public class Answer {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private int answerId;

    @ManyToMany(mappedBy = "answers")
    private Collection<Attempt> attempts;

    @Column(name = "answer_description")
    private String answerDescription;

    @Column(name = "answer_correct")
    private int answerCorrect;

    @Column(name = "answer_image")
    private String answerImage;
}
