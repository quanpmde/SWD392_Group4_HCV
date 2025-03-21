package com.ojt_Project.OJT_Project_11_21.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor

public class UserMe {
    private int userId;
    private String userEmail;
    private String userName;
    private String userRole;
    private String userPhone;
    private String userDob;
    private String userImage;
    private int userVip;
    private int userBalance;
}
