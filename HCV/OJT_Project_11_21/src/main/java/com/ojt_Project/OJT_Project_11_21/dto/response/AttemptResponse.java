package com.ojt_Project.OJT_Project_11_21.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class AttemptResponse {
    private int attemptId;
    private int userId;
    private int examId;
    private List<QuestionResponse> questions;
    private List<AnswerResponse> answers;
    private int attemptCount;
    private LocalDateTime attemptStartDate;
    private LocalDateTime attemptEndDate;
    private Double attemptPoint;
    private String attemptStatus;
    private int attemptCorrectAnswerCount;
    private String attemptTimeSubmitted;

}
