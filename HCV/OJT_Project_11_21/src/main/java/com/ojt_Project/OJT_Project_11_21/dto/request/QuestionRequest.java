package com.ojt_Project.OJT_Project_11_21.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class QuestionRequest {
    private int questionId;
    private int questionBankId;
    private String questionDescription;
    private String questionAnswerExplain;
    private MultipartFile questionImage;
}
