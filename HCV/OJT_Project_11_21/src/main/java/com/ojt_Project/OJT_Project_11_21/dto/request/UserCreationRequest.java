package com.ojt_Project.OJT_Project_11_21.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    String userName;
    String userEmail;
    @Size(min = 8, message = "PASSWORD_INVALID")
    String userPassword;
    @Size(min = 10, message = "PHONE_INVALID")
    String userPhone;
    String userStatus;
}
