package com.ojt_Project.OJT_Project_11_21.dto.request;

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
public class TestRequest {
    private int testId;
    private int userId;
    private int examId;
    private List<Integer> questionIds;
    private List<Integer> answerIds;
    private LocalDateTime testStartDate;
    private LocalDateTime testEndDate;
    private int testAttempt;
    private Double testPoint;
    private String testStatus;
    private int testCorrectAnswerCount;
    private String testTimeSubmitted;
}
