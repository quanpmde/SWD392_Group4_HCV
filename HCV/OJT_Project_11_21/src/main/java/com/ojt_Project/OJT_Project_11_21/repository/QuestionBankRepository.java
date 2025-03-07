package com.ojt_Project.OJT_Project_11_21.repository;

import com.ojt_Project.OJT_Project_11_21.entity.QuestionBank;
import com.ojt_Project.OJT_Project_11_21.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionBankRepository extends JpaRepository<QuestionBank, Integer> {
    List<QuestionBank> findByUser(User user);

    QuestionBank findByQuestionBankName(String questionBankName);
}
