package com.ojt_Project.OJT_Project_11_21.service;

import com.ojt_Project.OJT_Project_11_21.dto.request.QuestionRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.QuestionResponse;
import com.ojt_Project.OJT_Project_11_21.entity.Question;
import com.ojt_Project.OJT_Project_11_21.exception.AppException;
import com.ojt_Project.OJT_Project_11_21.exception.ErrorCode;
import com.ojt_Project.OJT_Project_11_21.mapper.AnswerMapper;
import com.ojt_Project.OJT_Project_11_21.mapper.QuestionMapper;
import com.ojt_Project.OJT_Project_11_21.repository.QuestionRepository;
import com.ojt_Project.OJT_Project_11_21.util.FileUtil;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@NoArgsConstructor
public class QuestionService {
    private static final String UPLOAD_DIR ="C:\\Users\\Admin\\Downloads\\OJT_Project_11_21\\img\\";
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerMapper answerMapper;

    public QuestionResponse createNewQuestion(QuestionRequest request) throws IOException{
        Question question = questionMapper.toQuestion(request);

        String relativeImagePath = FileUtil.saveImage(request.getQuestionImage(),UPLOAD_DIR);
        question.setQuestionImage(relativeImagePath);

        return questionMapper.toQuestionResponse(questionRepository.save(question));
    }

    public List<QuestionResponse> getAllQuestions() {
        List<Question> questions = questionRepository.findAll();
        return questions.stream()
                .map(questionMapper::toQuestionResponse)
                .toList();
    }

    public QuestionResponse getQuestionById(int questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_EXISTED));

        QuestionResponse response = questionMapper.toQuestionResponse(question);
        response.setAnswers(question.getAnswers().stream()
                .map(answerMapper::toAnswerResponse)
                .toList());

        return questionMapper.toQuestionResponse(question);
    }

    public QuestionResponse updateQuestionById(int questionId, QuestionRequest request) throws IOException {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_EXISTED));

        String relativeImagePath = FileUtil.saveImage(request.getQuestionImage(),UPLOAD_DIR);
        question.setQuestionImage(relativeImagePath);

        questionMapper.updateQuestionFromRequest(question, request);
        return questionMapper.toQuestionResponse(questionRepository.save(question));
    }
}
