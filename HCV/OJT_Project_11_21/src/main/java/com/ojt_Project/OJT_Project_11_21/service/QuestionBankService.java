package com.ojt_Project.OJT_Project_11_21.service;

import com.ojt_Project.OJT_Project_11_21.dto.request.QuestionBankRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.QuestionBankResponse;
import com.ojt_Project.OJT_Project_11_21.dto.response.SubjectResponse;
import com.ojt_Project.OJT_Project_11_21.entity.QuestionBank;
import com.ojt_Project.OJT_Project_11_21.entity.User;
import com.ojt_Project.OJT_Project_11_21.exception.AppException;
import com.ojt_Project.OJT_Project_11_21.exception.ErrorCode;
import com.ojt_Project.OJT_Project_11_21.mapper.QuestionBankMapper;
import com.ojt_Project.OJT_Project_11_21.repository.QuestionBankRepository;
import com.ojt_Project.OJT_Project_11_21.repository.UserRepository;
import com.ojt_Project.OJT_Project_11_21.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionBankService {
    private static final String UPLOAD_DIR ="C:\\Users\\Admin\\Downloads\\OJT_Project_11_21\\img\\";
    @Autowired
    private QuestionBankMapper questionBankMapper;
    @Autowired
    private QuestionBankRepository questionBankRepository;
    @Autowired
    private UserRepository userRepository;

    public QuestionBankResponse createNewQuestionBank(QuestionBankRequest request) throws IOException {
        // Kiểm tra xem userId có tồn tại không
        System.out.println(request.getUserId());
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_IS_NOT_FOUNDED));

        // Ánh xạ request thành QuestionBank và thiết lập user
        QuestionBank questionBank = questionBankMapper.toQuestionBank(request);
        questionBank.setUser(user);  // Thiết lập user
        questionBank.setQuestionBankDate(LocalDateTime.now());

        String relativeImagePath = FileUtil.saveImage(request.getQuestionBankImage(),UPLOAD_DIR);
        questionBank.setQuestionBankImage(relativeImagePath);

        return questionBankMapper.toQuestionBankResponse(questionBankRepository.save(questionBank));
    }

    public List<QuestionBankResponse> getAllQuestionBanks() {
        List<QuestionBank> questionBanks = questionBankRepository.findAll();
        return questionBanks.stream()
                .map(questionBankMapper::toQuestionBankResponse)
                .toList();
    }

    public QuestionBankResponse getQuestionBankById(int questionBankId) {
        QuestionBank questionBank = questionBankRepository.findById(questionBankId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTIONBANK_NOT_EXISTED));

        // Tải thủ công các câu hỏi và câu trả lời
        questionBank.getQuestions().forEach(question -> {
            question.getAnswers().size();  // Tải danh sách câu trả lời
        });

        return questionBankMapper.toQuestionBankResponse(questionBank);
    }

    public QuestionBankResponse updateQuestionBankById(int questionBankId, QuestionBankRequest request) throws IOException{
        QuestionBank questionBank = questionBankRepository.findById(questionBankId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTIONBANK_NOT_EXISTED));

        // Nếu cần cập nhật user thì tìm user theo userId
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_IS_NOT_FOUNDED));

        questionBank.setUser(user);  // Cập nhật user

        String relativeImagePath = FileUtil.saveImage(request.getQuestionBankImage(),UPLOAD_DIR);
        questionBank.setQuestionBankImage(relativeImagePath);

        questionBankMapper.updateQuestionBankFromRequest(questionBank, request);

        return questionBankMapper.toQuestionBankResponse(questionBankRepository.save(questionBank));
    }

}
