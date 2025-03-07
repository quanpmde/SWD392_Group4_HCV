package com.ojt_Project.OJT_Project_11_21.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor

public class UserResponse {
    private int userId;
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private String image;
    private int isBanned;
    private boolean isVip;
    private String role;
    private int active;
}
