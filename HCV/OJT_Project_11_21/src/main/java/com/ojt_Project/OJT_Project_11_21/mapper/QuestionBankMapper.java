package com.ojt_Project.OJT_Project_11_21.mapper;

import com.ojt_Project.OJT_Project_11_21.dto.request.QuestionBankRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.QuestionBankResponse;
import com.ojt_Project.OJT_Project_11_21.entity.QuestionBank;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class, ImageMapper.class},nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface QuestionBankMapper {
    @Mapping(source = "questionBankImage",target = "questionBankImage",qualifiedByName = "mapImage")
    QuestionBank toQuestionBank(QuestionBankRequest request);

    QuestionBankResponse toQuestionBankResponse(QuestionBank questionBank);

    @Mapping(target = "questionBankImage",ignore = true)
    void updateQuestionBankFromRequest(@MappingTarget QuestionBank questionBank, QuestionBankRequest request);

}