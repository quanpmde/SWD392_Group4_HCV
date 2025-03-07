package com.ojt_Project.OJT_Project_11_21.controller;

import com.ojt_Project.OJT_Project_11_21.dto.request.AnswerRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.ApiResponse;
import com.ojt_Project.OJT_Project_11_21.dto.response.AnswerResponse;
import com.ojt_Project.OJT_Project_11_21.service.AnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/answer")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AnswerController {
    @Autowired
    private AnswerService answerService;

    @PostMapping
    public ApiResponse<AnswerResponse> createNewAnswer(@ModelAttribute @RequestBody @Valid AnswerRequest request) throws IOException {
        return ApiResponse.<AnswerResponse>builder()
                .result(answerService.createNewAnswer(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<AnswerResponse>> getAllAnswers() {
        return ApiResponse.<List<AnswerResponse>>builder()
                .result(answerService.getAllAnswers())
                .build();
    }

    @GetMapping("/{answerId}")
    public ApiResponse<AnswerResponse> getAnswerById(@PathVariable int answerId) {
        return ApiResponse.<AnswerResponse>builder()
                .result(answerService.getAnswerById(answerId))
                .build();
    }

    @PutMapping("/{answerId}")
    public ApiResponse<AnswerResponse> updateAnswerById(@PathVariable int answerId,@ModelAttribute @RequestBody @Valid AnswerRequest request) throws IOException{
        return ApiResponse.<AnswerResponse>builder()
                .result(answerService.updateAnswerById(answerId, request))
                .build();
    }
}