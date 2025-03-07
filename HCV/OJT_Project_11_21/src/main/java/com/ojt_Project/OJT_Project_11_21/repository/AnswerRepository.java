package com.ojt_Project.OJT_Project_11_21.repository;

import com.ojt_Project.OJT_Project_11_21.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer,Integer> {
    List<Answer> findByQuestion_QuestionId(int questionId);
}
