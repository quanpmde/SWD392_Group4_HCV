package com.ojt_Project.OJT_Project_11_21.repository;

import com.ojt_Project.OJT_Project_11_21.entity.Exam;
import com.ojt_Project.OJT_Project_11_21.entity.Subject;
import com.ojt_Project.OJT_Project_11_21.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam,Integer> {
    List<Exam> findByUser(User user);
    List<Exam> findBySubject_SubjectId(int subjectId);

    // Lấy 5 Exam gần nhất của một Subject theo thời gian bắt đầu, giảm dần
    List<Exam> findTop5BySubjectOrderByExamStartDateDesc(Subject subject);

    // Lấy tất cả Exam của một Subject theo thời gian bắt đầu, giảm dần
    List<Exam> findBySubject_SubjectIdOrderByExamStartDateDesc(int subjectId);

}
