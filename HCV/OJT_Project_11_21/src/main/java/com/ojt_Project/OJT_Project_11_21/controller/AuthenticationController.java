package com.ojt_Project.OJT_Project_11_21.controller;

import com.ojt_Project.OJT_Project_11_21.dto.request.AuthenticationRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.ApiResponse;
import com.ojt_Project.OJT_Project_11_21.dto.response.AuthenticationResponse;
import com.ojt_Project.OJT_Project_11_21.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest){
        var result = authenticationService.login(authenticationRequest);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }
}
