package com.ojt_Project.OJT_Project_11_21.mapper;

import com.ojt_Project.OJT_Project_11_21.dto.request.ExamRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.ExamResponse;
import com.ojt_Project.OJT_Project_11_21.dto.response.QuestionBankResponse;
import com.ojt_Project.OJT_Project_11_21.entity.Exam;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",uses = {QuestionBankMapper.class, ImageMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExamMapper {
    @Mapping(target = "questionBanks", ignore = true) // Bỏ qua để xử lý sau
    @Mapping(source = "examImage", target = "examImage",qualifiedByName = "mapImage")
    Exam toExam(ExamRequest request);

    ExamResponse toExamResponse(Exam exam);

    @Mapping(target = "examImage",ignore = true)
    void updateExamFromRequest(@MappingTarget Exam exam, ExamRequest request);

}
