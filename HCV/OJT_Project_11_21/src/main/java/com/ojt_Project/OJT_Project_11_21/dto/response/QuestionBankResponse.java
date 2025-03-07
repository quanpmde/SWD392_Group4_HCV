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
public class QuestionBankResponse {
    private int questionBankID;
    private String questionBankName;
    private LocalDateTime questionBankDate;
    private String questionBankDescription;
    private String questionBankStatus;
    private String questionBankImage;
    private List<QuestionResponse> questions;
}
