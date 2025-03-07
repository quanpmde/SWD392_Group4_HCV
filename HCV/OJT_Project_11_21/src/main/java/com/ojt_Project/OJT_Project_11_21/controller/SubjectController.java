package com.ojt_Project.OJT_Project_11_21.controller;

import com.ojt_Project.OJT_Project_11_21.dto.request.SubjectRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.ApiResponse;
import com.ojt_Project.OJT_Project_11_21.dto.response.SubjectResponse;
import com.ojt_Project.OJT_Project_11_21.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/subject")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping
    public ApiResponse<SubjectResponse> createNewSubject(@ModelAttribute @RequestBody @Valid SubjectRequest request) throws IOException{
        return ApiResponse.<SubjectResponse>builder()
                .result(subjectService.createNewSubject(request))
                .build();
    }

    @GetMapping()
    public ApiResponse<List<SubjectResponse>> getAllSubject(){
        return ApiResponse.<List<SubjectResponse>>builder()
                .result(subjectService.getAllSubjects())
                .build();
    }

    @GetMapping("/{subjectId}")
    public ApiResponse<SubjectResponse> getSubjectById(@PathVariable int subjectId){
        return ApiResponse.<SubjectResponse>builder()
                .result(subjectService.getSubjectById(subjectId))
                .build();
    }

    @PutMapping("/{subjectId}")
    public ApiResponse<SubjectResponse> updateSubject(@PathVariable int subjectId,@ModelAttribute @RequestBody @Valid SubjectRequest request) throws IOException{
        return ApiResponse.<SubjectResponse>builder()
                .result(subjectService.updateSubject(subjectId, request))
                .build();
    }
}
