package com.ojt_Project.OJT_Project_11_21.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    private int userId;
    private String userEmail;
    private String userName;
    private String userPhone;
    private String userDob;
    private MultipartFile userImage;
    private int userVip;
    private int userBalance;
    private int userStatus;
}
