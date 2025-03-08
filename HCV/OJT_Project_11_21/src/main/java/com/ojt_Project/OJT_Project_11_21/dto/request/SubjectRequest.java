package com.ojt_Project.OJT_Project_11_21.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SubjectRequest {
    private int subjectId;
    private String subjectName;
    private MultipartFile subjectImage;
}
