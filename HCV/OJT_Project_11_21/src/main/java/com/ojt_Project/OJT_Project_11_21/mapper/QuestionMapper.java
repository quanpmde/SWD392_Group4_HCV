package com.ojt_Project.OJT_Project_11_21.mapper;

import com.ojt_Project.OJT_Project_11_21.dto.request.QuestionRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.QuestionResponse;
import com.ojt_Project.OJT_Project_11_21.entity.Question;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring", uses = ImageMapper.class, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface QuestionMapper {
    @Mapping(source = "questionBankId", target = "questionBank.questionBankID")
    @Mapping(source = "questionImage", target = "questionImage",qualifiedByName = "mapImage")
    Question toQuestion(QuestionRequest request);

    @Mapping(source = "questionId", target = "questionId")
    @Mapping(source = "questionDescription", target = "questionDescription")
    @Mapping(source = "questionImage", target = "questionImage")
    QuestionResponse toQuestionResponse(Question question);

    @Mapping(source = "questionBankId", target = "questionBank.questionBankID")
    @Mapping(target = "questionImage",ignore = true)
    void updateQuestionFromRequest(@MappingTarget Question question, QuestionRequest request);
}
