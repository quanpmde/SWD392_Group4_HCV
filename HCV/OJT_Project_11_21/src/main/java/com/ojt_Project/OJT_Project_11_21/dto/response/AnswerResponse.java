package com.ojt_Project.OJT_Project_11_21.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class AnswerResponse {
    private int questionId;
    private int answerId;
    private String answerDescription;
    private int answerCorrect;
    private String answerImage;
}