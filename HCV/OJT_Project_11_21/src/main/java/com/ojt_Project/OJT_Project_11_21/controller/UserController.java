package com.ojt_Project.OJT_Project_11_21.controller;

import com.nimbusds.openid.connect.sdk.UserInfoRequest;
import com.ojt_Project.OJT_Project_11_21.dto.request.LogOutRequest;
import com.ojt_Project.OJT_Project_11_21.dto.request.UserRegisterRequest;
import com.ojt_Project.OJT_Project_11_21.dto.request.UserUpdateRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.ApiResponse;
import com.ojt_Project.OJT_Project_11_21.dto.response.UserMe;
import com.ojt_Project.OJT_Project_11_21.dto.response.UserResponse;
import com.ojt_Project.OJT_Project_11_21.exception.AppException;
import com.ojt_Project.OJT_Project_11_21.exception.ErrorCode;
import com.ojt_Project.OJT_Project_11_21.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody @Valid UserRegisterRequest request) throws IOException {
        return ApiResponse.<String>builder()
                .result(userService.createNewUser(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers(){
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{email}")
    public ApiResponse<UserResponse> getUserByEmail(@PathVariable String email){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserByEmail(email))
                .build();
    }

    @PutMapping("/{email}")
    public ApiResponse<UserResponse> updateUserByEmail(@PathVariable String email, @RequestBody @Valid UserUpdateRequest request) throws IOException{
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUserByEmail(email,request))
                .build();
    }
    
    @PutMapping("/update-user")
    public ApiResponse<UserMe> updateUser(@RequestBody @Valid UserUpdateRequest request) throws IOException{
        return ApiResponse.<UserMe>builder()
                .result(userService.updateUser(request))
                .build();
    }
    
    @PutMapping("/image/{id}")
    public ApiResponse<UserMe> updateUserImage(@PathVariable String id,@RequestPart(value = "file", required = false) MultipartFile file) throws IOException{
        return ApiResponse.<UserMe>builder()
                .result(userService.updateUserImage(id,file))
                .build();
    } 
    //tong hop vua json vua file
    @PutMapping("/update-me")
    public ApiResponse<UserMe> updateUserInfo(@RequestParam("data") String data,@RequestPart(value = "file", required = false) MultipartFile file) throws IOException{
        return ApiResponse.<UserMe>builder()
                .result(userService.updateUserInfo(data,file))
                .build();
    }
    
    @GetMapping("/userme")
    public ApiResponse<UserMe> getMyInfomation(){
        return ApiResponse.<UserMe>builder()
                .result(userService.getMyInfomation())
                .build();
    }

}
