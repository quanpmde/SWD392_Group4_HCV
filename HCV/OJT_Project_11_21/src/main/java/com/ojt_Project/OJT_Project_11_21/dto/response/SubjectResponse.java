package com.ojt_Project.OJT_Project_11_21.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SubjectResponse {
    private int subjectId;
    private String subjectName;
    private String subjectImage;
}
