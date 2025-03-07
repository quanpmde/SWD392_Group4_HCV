package com.ojt_Project.OJT_Project_11_21.controller;

import com.ojt_Project.OJT_Project_11_21.dto.request.TestRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.ApiResponse;
import com.ojt_Project.OJT_Project_11_21.dto.response.TestResponse;
import com.ojt_Project.OJT_Project_11_21.dto.response.UserTestResponse;
import com.ojt_Project.OJT_Project_11_21.service.TestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TestController {
    @Autowired
    private TestService testService;

    @PostMapping
    public ApiResponse<TestResponse> createNewTest(@RequestBody @Valid TestRequest request) throws IOException{
        return ApiResponse.<TestResponse>builder()
                .result(testService.createNewTest(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<TestResponse>> getAllTests(){
        return ApiResponse.<List<TestResponse>>builder()
                .result(testService.getAllTests())
                .build();
    }

    @GetMapping("/{testId}")
    public ApiResponse<TestResponse> getTestById(@PathVariable int testId) {
        return ApiResponse.<TestResponse>builder()
                .result(testService.getTestById(testId))
                .build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<TestResponse>> getTestByUserId(@PathVariable int userId){
        return ApiResponse.<List<TestResponse>>builder()
                .result(testService.getTestByUserId(userId))
                .build();
    }

    @GetMapping("/user/{userId}/exam/{examId}")
    public ApiResponse<List<TestResponse>> getTestByUserIdAndExamId(@PathVariable int userId, @PathVariable int examId){
        return ApiResponse.<List<TestResponse>>builder()
                .result(testService.getTestsByUserIdAndExamId(userId,examId))
                .build();
    }

    @GetMapping("/top-tests/exam/{examId}")
    public ApiResponse<List<TestResponse>> getTopTestsByExamId(@PathVariable int examId) {
        return ApiResponse.<List<TestResponse>>builder()
                .result(testService.getTopTestsByExamId(examId))
                .build();
    }

    @GetMapping("/top-users/exam/{examId}")
    public ApiResponse<List<UserTestResponse>> getTopUsersByExamId(@PathVariable int examId) {
        return ApiResponse.<List<UserTestResponse>>builder()
                .result(testService.getTopUsersByExamId(examId))
                .build();
    }


    @PostMapping("/save/{testId}")
    public ApiResponse<TestResponse> saveTest(@PathVariable int testId,@RequestBody @Valid TestRequest request){
        return ApiResponse.<TestResponse>builder()
                .result(testService.saveTest(testId,request))
                .build();
    }

    @PutMapping("/{testId}")
    public ApiResponse<TestResponse> updateTest(@PathVariable int testId,@RequestBody @Valid TestRequest request) throws IOException{
        return ApiResponse.<TestResponse>builder()
                .result(testService.updateTest(testId, request))
                .build();
    }

    @PostMapping("/submit/{testId}")
    public ApiResponse<TestResponse> submitTest(@PathVariable int testId){
        return ApiResponse.<TestResponse>builder()
                .result(testService.submitTest(testId))
                .build();
    }

    @GetMapping("/time-left/{testId}")
    public ApiResponse<Long> getTimeLeft(@PathVariable int testId){
        long secondsLeft = testService.getTimeLeft(testId);
        return ApiResponse.<Long>builder()
                .result(secondsLeft)
                .build();
    }
}
