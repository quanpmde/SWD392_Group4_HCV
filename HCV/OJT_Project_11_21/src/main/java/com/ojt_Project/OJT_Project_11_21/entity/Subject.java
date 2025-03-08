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
    @Column(name = "subject_id")
    private int subjectId;

    @OneToMany (mappedBy = "subject",cascade = CascadeType.ALL)
    private List<QuestionBank> questionBanks;

    @OneToMany (mappedBy = "subject",cascade = CascadeType.ALL)
    private List<Exam> exams;

    @Column(name = "subject_name",length = 50)
    private String subjectName;

    @Column(name = "subject_image",length = 1000)
    private String subjectImage;
}
