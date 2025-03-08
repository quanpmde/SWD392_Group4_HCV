package com.ojt_Project.OJT_Project_11_21.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class QuestionBankRequest {
    private int userId;
    private int questionBankID;
    private String questionBankName;
    private LocalDateTime questionBankDate;
    private String questionBankDescription;
    private String questionBankStatus;
    private MultipartFile questionBankImage;
}
