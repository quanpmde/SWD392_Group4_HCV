package com.ojt_Project.OJT_Project_11_21.repository;

import com.ojt_Project.OJT_Project_11_21.entity.Exam;
import com.ojt_Project.OJT_Project_11_21.entity.Test;
import com.ojt_Project.OJT_Project_11_21.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TestRepository extends JpaRepository<Test, Integer> {

    Optional<Test> findTopByUserAndExamOrderByTestStartDateDesc(User user, Exam exam);

    List<Test> findAllByUser_UserIdAndExam_ExamId(int userId, int examId);
    
    List<Test> findAllByTestStatus(String testStatus);

    List<Test> findAllByExam_ExamId(int examId);

    List<Test> findAllByUser_UserId(int userId);

    List<Test> findAllByExam_ExamIdAndTestStatus(int examId, String testStatus);
}
