package com.ojt_Project.OJT_Project_11_21.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SubjectRequest {
    private int subjectId;
    private String subjectName;
    private MultipartFile subjectImage;
}
