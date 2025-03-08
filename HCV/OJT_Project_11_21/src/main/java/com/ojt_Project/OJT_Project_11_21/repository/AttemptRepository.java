package com.ojt_Project.OJT_Project_11_21.repository;

import com.ojt_Project.OJT_Project_11_21.entity.Attempt;
import com.ojt_Project.OJT_Project_11_21.entity.Exam;
import com.ojt_Project.OJT_Project_11_21.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttemptRepository extends JpaRepository<Attempt, Integer> {

    Optional<Attempt> findTopByUserAndExamOrderByAttemptStartDateDesc(User user, Exam exam);

    List<Attempt> findAllByUser_UserIdAndExam_ExamId(int userId, int examId);
    
    List<Attempt> findAllByAttemptStatus(String attemptStatus);

    List<Attempt> findAllByExam_ExamId(int examId);

    List<Attempt> findAllByUser_UserId(int userId);

    List<Attempt> findAllByExam_ExamIdAndAttemptStatus(int examId, String attemptStatus);
}
