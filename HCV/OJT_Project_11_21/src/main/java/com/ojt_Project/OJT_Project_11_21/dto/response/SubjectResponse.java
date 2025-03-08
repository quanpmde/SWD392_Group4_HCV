package com.ojt_Project.OJT_Project_11_21.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SubjectResponse {
    private int subjectId;
    private String subjectName;
    private String subjectImage;
}
