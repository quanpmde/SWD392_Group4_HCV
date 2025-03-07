package com.ojt_Project.OJT_Project_11_21.dto.response;

import com.ojt_Project.OJT_Project_11_21.entity.QuestionBank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ExamResponse {
    private int userId;
    private String userImage;
    private String fullName;
    private String userRole;
    private int subjectId;
    private String subjectName;
    private int examId;
    private String examName;
    private String examPassword;
    private int examTotalQuestions;
    private int examTimer;
    private int examAttempt;
    private int examLikeCount;
    private int examViewCount;
    private int examCommentCount;
    private LocalDateTime examStartDate;
    private LocalDateTime examEndDate;
    private String examImage;
    private List<QuestionBankResponse> questionBanks;
    private List<QuestionResponse> questions;
    private String examStatus;

}