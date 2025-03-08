package com.ojt_Project.OJT_Project_11_21.dto.response;

import com.ojt_Project.OJT_Project_11_21.entity.Answer;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class QuestionResponse {
    private int questionId;
    private String questionDescription;
    private String questionAnswerExplain;
    private String questionImage;
    private List<AnswerResponse> answers;
}
