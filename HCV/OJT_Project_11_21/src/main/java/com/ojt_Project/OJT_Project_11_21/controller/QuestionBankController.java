package com.ojt_Project.OJT_Project_11_21.controller;

import com.ojt_Project.OJT_Project_11_21.dto.request.QuestionBankRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.ApiResponse;
import com.ojt_Project.OJT_Project_11_21.dto.response.QuestionBankResponse;
import com.ojt_Project.OJT_Project_11_21.service.QuestionBankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/questionBank")
@RequiredArgsConstructor
@CrossOrigin("*")
public class QuestionBankController {
    @Autowired
    private QuestionBankService questionBankService;

    @PostMapping
    public ApiResponse<QuestionBankResponse> createNewQuestionBank(@ModelAttribute @RequestBody @Valid QuestionBankRequest request) throws IOException{
        return ApiResponse.<QuestionBankResponse>builder()
                .result(questionBankService.createNewQuestionBank(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<QuestionBankResponse>> getAllQuestionBank(){
        return ApiResponse.<List<QuestionBankResponse>>builder()
                .result(questionBankService.getAllQuestionBanks())
                .build();
    }

    @GetMapping("/{questionBankId}")
    public ApiResponse<QuestionBankResponse> getQuestionBankById(@PathVariable int questionBankId){
        return ApiResponse.<QuestionBankResponse>builder()
                .result(questionBankService.getQuestionBankById(questionBankId))
                .build();
    }

    @PutMapping
    public ApiResponse<QuestionBankResponse> updateQuestionBankById(@PathVariable int questionBankId,@ModelAttribute @RequestBody @Valid QuestionBankRequest request) throws IOException{
        return ApiResponse.<QuestionBankResponse>builder()
                .result(questionBankService.updateQuestionBankById(questionBankId,request))
                .build();
    }
}
