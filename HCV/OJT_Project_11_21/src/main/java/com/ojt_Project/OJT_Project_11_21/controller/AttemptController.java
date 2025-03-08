package com.ojt_Project.OJT_Project_11_21.controller;

import com.ojt_Project.OJT_Project_11_21.dto.request.AttemptRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.ApiResponse;
import com.ojt_Project.OJT_Project_11_21.dto.response.AttemptResponse;
import com.ojt_Project.OJT_Project_11_21.dto.response.UserAttemptResponse;
import com.ojt_Project.OJT_Project_11_21.service.AttemptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/attempt")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AttemptController {
    @Autowired
    private AttemptService attemptService;

    @PostMapping
    public ApiResponse<AttemptResponse> createNewAttempt(@RequestBody @Valid AttemptRequest request) throws IOException{
        return ApiResponse.<AttemptResponse>builder()
                .result(attemptService.createNewAttempt(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<AttemptResponse>> getAllAttempts(){
        return ApiResponse.<List<AttemptResponse>>builder()
                .result(attemptService.getAllAttempts())
                .build();
    }

    @GetMapping("/{attemptId}")
    public ApiResponse<AttemptResponse> getAttemptById(@PathVariable int attemptId) {
        return ApiResponse.<AttemptResponse>builder()
                .result(attemptService.getAttemptById(attemptId))
                .build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<AttemptResponse>> getAttemptByUserId(@PathVariable int userId){
        return ApiResponse.<List<AttemptResponse>>builder()
                .result(attemptService.getAttemptByUserId(userId))
                .build();
    }

    @GetMapping("/user/{userId}/exam/{examId}")
    public ApiResponse<List<AttemptResponse>> getAttemptByUserIdAndExamId(@PathVariable int userId, @PathVariable int examId){
        return ApiResponse.<List<AttemptResponse>>builder()
                .result(attemptService.getAttemptsByUserIdAndExamId(userId,examId))
                .build();
    }

    @GetMapping("/top-attempts/exam/{examId}")
    public ApiResponse<List<AttemptResponse>> getTopAttemptsByExamId(@PathVariable int examId) {
        return ApiResponse.<List<AttemptResponse>>builder()
                .result(attemptService.getTopAttemptsByExamId(examId))
                .build();
    }

    @GetMapping("/top-users/exam/{examId}")
    public ApiResponse<List<UserAttemptResponse>> getTopUsersByExamId(@PathVariable int examId) {
        return ApiResponse.<List<UserAttemptResponse>>builder()
                .result(attemptService.getTopUsersByExamId(examId))
                .build();
    }


    @PostMapping("/save/{attemptId}")
    public ApiResponse<AttemptResponse> saveAttempt(@PathVariable int attemptId, @RequestBody @Valid AttemptRequest request){
        return ApiResponse.<AttemptResponse>builder()
                .result(attemptService.saveAttempt(attemptId,request))
                .build();
    }

    @PutMapping("/{attemptId}")
    public ApiResponse<AttemptResponse> updateAttempt(@PathVariable int attemptId, @RequestBody @Valid AttemptRequest request) throws IOException{
        return ApiResponse.<AttemptResponse>builder()
                .result(attemptService.updateAttempt(attemptId, request))
                .build();
    }

    @PostMapping("/submit/{attemptId}")
    public ApiResponse<AttemptResponse> submitAttempt(@PathVariable int attemptId){
        return ApiResponse.<AttemptResponse>builder()
                .result(attemptService.submitAttempt(attemptId))
                .build();
    }

    @GetMapping("/time-left/{attemptId}")
    public ApiResponse<Long> getTimeLeft(@PathVariable int attemptId){
        long secondsLeft = attemptService.getTimeLeft(attemptId);
        return ApiResponse.<Long>builder()
                .result(secondsLeft)
                .build();
    }
}
