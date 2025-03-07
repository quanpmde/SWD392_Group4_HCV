package com.ojt_Project.OJT_Project_11_21.controller;

import com.ojt_Project.OJT_Project_11_21.dto.request.UserRegisterRequest;
import com.ojt_Project.OJT_Project_11_21.dto.request.UserUpdateRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.ApiResponse;
import com.ojt_Project.OJT_Project_11_21.dto.response.UserResponse;
import com.ojt_Project.OJT_Project_11_21.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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

}
