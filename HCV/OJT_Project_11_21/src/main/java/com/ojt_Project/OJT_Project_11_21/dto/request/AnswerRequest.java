package com.ojt_Project.OJT_Project_11_21.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class AnswerRequest {
    private int questionId;
    private int answerId;
    private String answerDescription;
    private int answerCorrect;
    private MultipartFile answerImage;
}