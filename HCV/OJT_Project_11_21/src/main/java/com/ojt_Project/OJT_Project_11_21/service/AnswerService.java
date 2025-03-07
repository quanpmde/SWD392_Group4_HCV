package com.ojt_Project.OJT_Project_11_21.service;

import com.ojt_Project.OJT_Project_11_21.dto.request.AnswerRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.AnswerResponse;
import com.ojt_Project.OJT_Project_11_21.entity.Answer;
import com.ojt_Project.OJT_Project_11_21.entity.Question;
import com.ojt_Project.OJT_Project_11_21.exception.AppException;
import com.ojt_Project.OJT_Project_11_21.exception.ErrorCode;
import com.ojt_Project.OJT_Project_11_21.mapper.AnswerMapper;
import com.ojt_Project.OJT_Project_11_21.repository.AnswerRepository;
import com.ojt_Project.OJT_Project_11_21.repository.QuestionRepository;
import com.ojt_Project.OJT_Project_11_21.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private static final String UPLOAD_DIR ="C:\\Users\\Admin\\Downloads\\OJT_Project_11_21\\img\\";
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;

    public AnswerResponse createNewAnswer(AnswerRequest request) throws IOException {
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_EXISTED));

        Answer answer = answerMapper.toAnswer(request);
        answer.setQuestion(question);

        String relativeImagePath = FileUtil.saveImage(request.getAnswerImage(),UPLOAD_DIR);
        answer.setAnswerImage(relativeImagePath);

        AnswerResponse answerResponse = answerMapper.toAnswerResponse(answerRepository.save(answer));
        return answerResponse;
    }

    public List<AnswerResponse> getAllAnswers() {
        List<Answer> answers = answerRepository.findAll();
        return answers.stream()
                .map(answerMapper::toAnswerResponse)
                .toList();
    }

    public AnswerResponse getAnswerById(int answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_EXISTED));
        AnswerResponse answerResponse = answerMapper.toAnswerResponse(answerRepository.save(answer));
        return answerResponse;
    }

    public AnswerResponse updateAnswerById(int answerId, AnswerRequest request) throws IOException {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_EXISTED));

        String relativeImagePath = FileUtil.saveImage(request.getAnswerImage(),UPLOAD_DIR);
        answer.setAnswerImage(relativeImagePath);

        answerMapper.updateAnswerFromRequest(answer, request);
        return answerMapper.toAnswerResponse(answerRepository.save(answer));
    }
}