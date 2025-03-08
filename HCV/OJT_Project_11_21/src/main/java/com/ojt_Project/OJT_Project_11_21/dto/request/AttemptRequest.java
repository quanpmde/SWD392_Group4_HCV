package com.ojt_Project.OJT_Project_11_21.dto.request;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class AttemptRequest {
    private int attemptId;
    private int userId;
    private int examId;
    private List<Integer> questionIds;
    private List<Integer> answerIds;
    private int attemptCount;
    private LocalDateTime attemptStartDate;
    private LocalDateTime attemptEndDate;
    private Double attemptPoint;
    private String attemptStatus;
    private int attemptCorrectAnswerCount;
    private String attemptTimeSubmitted;

}
