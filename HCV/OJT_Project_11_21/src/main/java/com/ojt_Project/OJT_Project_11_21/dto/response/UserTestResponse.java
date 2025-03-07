package com.ojt_Project.OJT_Project_11_21.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserTestResponse {
    private int userId;
    private int examId;
    private double testPoint;
    private String testTimeSubmitted; // Giả định bạn đã lưu thời gian nộp bài dưới dạng String
}