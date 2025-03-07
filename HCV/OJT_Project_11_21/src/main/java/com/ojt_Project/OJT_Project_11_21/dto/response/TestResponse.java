package com.ojt_Project.OJT_Project_11_21.dto.response;

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
public class TestResponse {
    private int testId;
    private int userId;
    private int examId;
    private List<QuestionResponse> questions;
    private List<AnswerResponse> answers;
    private int testAttempt;
    private Double testPoint;
    private int testCorrectAnswerCount;
    private LocalDateTime testStartDate;
    private LocalDateTime testEndDate;
    private String testTimeSubmitted;
    private String testStatus;

}
