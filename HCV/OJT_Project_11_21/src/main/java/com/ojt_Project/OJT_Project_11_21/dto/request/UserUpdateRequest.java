package com.ojt_Project.OJT_Project_11_21.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    private int userId;
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private MultipartFile image;
    private int isBanned;
    private boolean isVip;
    private String role;
    private int active;
}
