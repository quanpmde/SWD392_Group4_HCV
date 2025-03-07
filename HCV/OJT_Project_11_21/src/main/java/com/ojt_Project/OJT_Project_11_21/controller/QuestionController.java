package com.ojt_Project.OJT_Project_11_21.controller;

import com.ojt_Project.OJT_Project_11_21.dto.request.QuestionRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.ApiResponse;
import com.ojt_Project.OJT_Project_11_21.dto.response.QuestionResponse;
import com.ojt_Project.OJT_Project_11_21.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
@CrossOrigin("*")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @PostMapping
    public ApiResponse<QuestionResponse> createNewQuestion(@ModelAttribute @RequestBody @Valid QuestionRequest request) throws IOException{
        return ApiResponse.<QuestionResponse>builder()
                .result(questionService.createNewQuestion(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<QuestionResponse>> getAllQuestions() {
        return ApiResponse.<List<QuestionResponse>>builder()
                .result(questionService.getAllQuestions())
                .build();
    }

    @GetMapping("/{questionId}")
    public ApiResponse<QuestionResponse> getQuestionById(@PathVariable int questionId) {
        return ApiResponse.<QuestionResponse>builder()
                .result(questionService.getQuestionById(questionId))
                .build();
    }

    @PutMapping("/{questionId}")
    public ApiResponse<QuestionResponse> updateQuestionById(@PathVariable int questionId,@ModelAttribute @RequestBody @Valid QuestionRequest request) throws IOException {
        return ApiResponse.<QuestionResponse>builder()
                .result(questionService.updateQuestionById(questionId, request))
                .build();
    }
}

