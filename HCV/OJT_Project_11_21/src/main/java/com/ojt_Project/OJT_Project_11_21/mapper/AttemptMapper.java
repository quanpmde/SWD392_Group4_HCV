package com.ojt_Project.OJT_Project_11_21.mapper;

import com.ojt_Project.OJT_Project_11_21.dto.request.AttemptRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.AttemptResponse;
import com.ojt_Project.OJT_Project_11_21.entity.Attempt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {QuestionMapper.class, AnswerMapper.class})
public interface AttemptMapper {
    Attempt toAttempt(AttemptRequest request);

    @Mapping(source = "user.userId", target = "userId") // Mapping userId từ entity sang response
    @Mapping(source = "exam.examId", target = "examId")
    @Mapping(source = "questions", target = "questions") // Sử dụng QuestionMapper
    @Mapping(source = "answers", target = "answers")     // Sử dụng AnswerMapper
    AttemptResponse toAttemptResponse(Attempt attempt);
}
