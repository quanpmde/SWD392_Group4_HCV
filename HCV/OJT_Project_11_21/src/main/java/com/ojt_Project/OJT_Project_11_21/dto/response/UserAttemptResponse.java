package com.ojt_Project.OJT_Project_11_21.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserAttemptResponse {
    private int userId;
    private int examId;
    private double attemptPoint;
    private String attemptTimeSubmitted; // Giả định bạn đã lưu thời gian nộp bài dưới dạng String
}