package com.ojt_Project.OJT_Project_11_21.mapper;

import com.ojt_Project.OJT_Project_11_21.dto.request.AnswerRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.AnswerResponse;
import com.ojt_Project.OJT_Project_11_21.entity.Answer;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring",uses = {AnswerMapper.class, ImageMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AnswerMapper {
    @Mapping(source = "questionId", target = "question.questionId")
    @Mapping(source = "answerImage",target = "answerImage",qualifiedByName = "mapImage")
    Answer toAnswer(AnswerRequest request);
    @Mapping(source = "question.questionId", target = "questionId")
    AnswerResponse toAnswerResponse(Answer answer);

    @Mapping(source = "questionId", target = "question.questionId")
    @Mapping(target = "answerImage",ignore = true)
    void updateAnswerFromRequest(@MappingTarget Answer answer, AnswerRequest request);

}