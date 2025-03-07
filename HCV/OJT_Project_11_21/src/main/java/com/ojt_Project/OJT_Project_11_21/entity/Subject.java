package com.ojt_Project.OJT_Project_11_21.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "Subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subjectId")
    private int subjectId;

    @OneToMany (mappedBy = "subject",cascade = CascadeType.ALL)
    private List<QuestionBank> questionBanks;

    @OneToMany (mappedBy = "subject",cascade = CascadeType.ALL)
    private List<Exam> exams;

    @Column(name = "subjectName",length = 50)
    private String subjectName;

    @Column(name = "subjectImage",length = 1000)
    private String subjectImage;
}
