package com.ojt_Project.OJT_Project_11_21.service;

import com.ojt_Project.OJT_Project_11_21.dto.request.ExamRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.ExamResponse;
import com.ojt_Project.OJT_Project_11_21.dto.response.QuestionResponse;
import com.ojt_Project.OJT_Project_11_21.entity.*;
import com.ojt_Project.OJT_Project_11_21.exception.AppException;
import com.ojt_Project.OJT_Project_11_21.exception.ErrorCode;
import com.ojt_Project.OJT_Project_11_21.mapper.ExamMapper;
import com.ojt_Project.OJT_Project_11_21.mapper.QuestionBankMapper;
import com.ojt_Project.OJT_Project_11_21.mapper.QuestionMapper;
import com.ojt_Project.OJT_Project_11_21.repository.*;
import com.ojt_Project.OJT_Project_11_21.util.DateUtil;
import com.ojt_Project.OJT_Project_11_21.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamService {
    private static final String UPLOAD_DIR ="C:\\Users\\Admin\\Downloads\\OJT_Project_11_21\\img\\";
    @Autowired
    private ExamMapper examMapper;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private QuestionBankMapper questionBankMapper;
    @Autowired
    private QuestionBankRepository questionBankRepository;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubjectRepostiory subjectRepository;
    @Autowired
    private DateUtil dateUtil;

    public ExamResponse createNewExam(ExamRequest request) throws IOException {
        // Tìm user theo userId
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_IS_NOT_FOUNDED));

        //Tìm subject theo subjectId
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_EXISTED));

        // Lấy tất cả các QuestionBank của user
        List<QuestionBank> userQuestionBanks = questionBankRepository.findByUser(user);

        if (userQuestionBanks.isEmpty()){
            throw new AppException(ErrorCode.QUESTIONBANK_IS_NOT_FOUNDED);
        }

        // Kiểm tra questionIds có thuộc về QuestionBank của User không
        List<Question> selectedQuestions = questionRepository.findAllById(request.getQuestionIds());

        for (Question question : selectedQuestions) {
            boolean belongsToUserBank = userQuestionBanks.stream()
                    .anyMatch(qb -> qb.getQuestionBankID() == question.getQuestionBank().getQuestionBankID());
            if (!belongsToUserBank) {
                throw new AppException(ErrorCode.SELECTED_QUESTIONS_INVALID);
            }
        }

        // Tạo mới Exam từ request
        Exam exam = examMapper.toExam(request);

        String relativeImagePath = FileUtil.saveImage(request.getExamImage(),UPLOAD_DIR);
        exam.setExamImage(relativeImagePath);

        // Thiết lập trạng thái ban đầu của bài thi
        if (LocalDateTime.now().isBefore(exam.getExamStartDate())) {
            exam.setExamStatus("pending");
        } else if (LocalDateTime.now().isAfter(exam.getExamEndDate())) {
            exam.setExamStatus("overdue");
        } else {
            exam.setExamStatus("active");
        }

        exam.setUser(user);
        exam.setSubject(subject);
        exam.setQuestions(selectedQuestions);
        exam.setExamTotalQuestions(selectedQuestions.size());

        // Thêm các câu hỏi random từ questionBank
        if (request.getRandomQuestionBanks() != null && !request.getRandomQuestionBanks().isEmpty()) {
            Map<QuestionBank, Integer> randomQuestionsMap = request.getRandomQuestionBanks().entrySet().stream()
                    .collect(Collectors.toMap(
                            entry -> questionBankRepository.findById(entry.getKey())
                                    .orElseThrow(() -> new AppException(ErrorCode.QUESTIONBANK_IS_NOT_FOUNDED)),
                            Map.Entry::getValue
                    ));

            addRandomQuestionsToExam(exam, randomQuestionsMap);
        }


        ExamResponse examResponse = examMapper.toExamResponse(examRepository.save(exam));
        examResponse.setUserId(user.getUserId());
        examResponse.setUserImage(user.getUserImage());
        examResponse.setUserName(user.getUserName());
        examResponse.setUserRole(user.getUserRole());
        examResponse.setSubjectId(subject.getSubjectId());
        examResponse.setSubjectName(subject.getSubjectName());

        return examResponse;
    }

    public ExamResponse addQuestionsFromFileToExamOrBank(String filePath, int examId, boolean isForExam) throws IOException {
        // Tìm exam theo examId
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_EXISTED));

        // Đọc câu hỏi từ file
        List<Question> questionsFromFile = FileUtil.readQuestionFromFile(filePath);

        if (isForExam) {
            // Thêm vào Exam mới tạo
            exam.getQuestions().addAll(questionsFromFile);
            exam.setExamTotalQuestions(exam.getQuestions().size());

            // Thêm các câu hỏi vào QuestionBank "Chưa có tiêu đề"
            QuestionBank questionBank = questionBankRepository.findByQuestionBankName("Chưa có tiêu đề");
            if (questionBank == null) {
                questionBank = new QuestionBank();
                questionBank.setQuestionBankDescription("Chưa có tiêu đề");
                questionBank.setQuestions(new ArrayList<>());
            }
            // Thêm câu hỏi vào QuestionBank
            questionBank.getQuestions().addAll(questionsFromFile);
            questionBankRepository.save(questionBank);

            // Lưu Exam
            examRepository.save(exam);
        }
        // Trả về ExamResponse đã cập nhật
        return examMapper.toExamResponse(exam);
    }

    public void addManualQuestionToExam(Exam exam, List<Integer> questionIds){
        List<Question> manualQuestions = questionRepository.findAllById(questionIds);
        exam.getQuestions().addAll(manualQuestions);
        // Cập nhật tổng số câu hỏi
        exam.setExamTotalQuestions(exam.getQuestions().size());
    }

    public void addRandomQuestionsToExam(Exam exam, Map<QuestionBank, Integer> randomQuestionsMap){
        for (Map.Entry<QuestionBank, Integer> entry : randomQuestionsMap.entrySet()){
            QuestionBank questionBank = entry.getKey();
            int numberOfRandomQuestions = entry.getValue();
            List<Question> availableQuestions = questionBank.getQuestions();

            //Trường hợp số câu trong questionBank không đủ số lượng cần random
            if (availableQuestions.size() < numberOfRandomQuestions){
                throw new AppException(ErrorCode.NOT_ENOUGH_QUESTION);
            }

            Collections.shuffle(availableQuestions); // Trộn ngẫu nhiên câu hỏi
            List<Question> selectedQuestions = availableQuestions.subList(0,numberOfRandomQuestions);
            exam.getQuestions().addAll(selectedQuestions);
        }

        // Cập nhật tổng số câu hỏi
        exam.setExamTotalQuestions(exam.getQuestions().size());
    }

    public List<ExamResponse> getAllExams() {
        List<Exam> exams = examRepository.findAll();
        return exams.stream()
                .map(exam -> {
                    ExamResponse examResponse = examMapper.toExamResponse(exam);

                    // Cập nhật các trường bị thiếu
                    examResponse.setUserId(exam.getUser().getUserId());
                    examResponse.setUserImage(exam.getUser().getUserImage());
                    examResponse.setUserName(exam.getUser().getUserName());
                    examResponse.setUserRole(exam.getUser().getUserRole());
                    examResponse.setSubjectId(exam.getSubject().getSubjectId());
                    examResponse.setSubjectName(exam.getSubject().getSubjectName());
                    return examResponse;
                })
                .collect(Collectors.toList());
    }

    public ExamResponse getExamById(int examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_EXISTED));

        // Tải thủ công các câu hỏi từ QuestionBank
        exam.getQuestionBanks().forEach(questionBank -> {
            questionBank.getQuestions().forEach(question -> {
                question.getAnswers().size();  // Tải danh sách câu trả lời
            });
        });

        // Thiết lập trạng thái ban đầu của bài thi
        if (LocalDateTime.now().isBefore(exam.getExamStartDate())) {
            exam.setExamStatus("pending");
        } else if (LocalDateTime.now().isAfter(exam.getExamEndDate())) {
            exam.setExamStatus("overdue");
        } else {
            exam.setExamStatus("active");
        }

        // Ánh xạ vào DTO để trả về cho người dùng
        ExamResponse examResponse = examMapper.toExamResponse(exam);
        examResponse.setUserId(exam.getUser().getUserId());
        examResponse.setUserName(exam.getUser().getUserName());
        examResponse.setSubjectId(exam.getSubject().getSubjectId());
        examResponse.setSubjectName(exam.getSubject().getSubjectName());

        // Ánh xạ danh sách questionBanks đã tải đủ dữ liệu
        examResponse.setQuestionBanks(exam.getQuestionBanks().stream()
                .map(questionBankMapper::toQuestionBankResponse)
                .toList());

        return examResponse;
    }

    public List<ExamResponse> getExamsByUserId(int userId) {
        // Tìm user theo userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_IS_NOT_FOUNDED));

        // Tìm các exam thuộc về user này
        List<Exam> exams = examRepository.findByUser(user);

        // Chuyển đổi danh sách exam thành danh sách ExamResponse
        return exams.stream()
                .map(examMapper::toExamResponse)
                .collect(Collectors.toList());
    }

    public List<ExamResponse> getRecentExamsBySubjectId(int subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_EXISTED));

        List<Exam> exams = examRepository.findTop5BySubjectOrderByExamStartDateDesc(subject);

        return exams.stream()
                .map(examMapper::toExamResponse)
                .collect(Collectors.toList());
    }


    public List<ExamResponse> getAllExamsBySubjectId(int subjectId) {
        // Tìm subject theo subjectId
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_EXISTED));

        // Tìm các exam thuộc về subject này
        List<Exam> exams = examRepository.findBySubject_SubjectId(subjectId);

        // Chuyển đổi danh sách exam thành danh sách ExamResponse
        return exams.stream()
                .map(exam -> {
                    ExamResponse examResponse = examMapper.toExamResponse(exam);

                    // Cập nhật các trường bị thiếu
                    examResponse.setUserId(exam.getUser().getUserId());
                    examResponse.setUserName(exam.getUser().getUserName());
                    examResponse.setSubjectId(exam.getSubject().getSubjectId());
                    examResponse.setSubjectName(exam.getSubject().getSubjectName());

                    return examResponse;
                })
                .collect(Collectors.toList());
    }




    public ExamResponse getTopTenQuestionsFromExam(int examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_EXISTED));

        // Lấy tối đa 10 câu hỏi
        List<Question> topTenQuestions = exam.getQuestions().stream()
                .limit(10) // Lấy tối đa 10 câu hỏi
                .collect(Collectors.toList());

        // Chuyển đổi danh sách câu hỏi thành danh sách QuestionResponse
        List<QuestionResponse> questionResponses = topTenQuestions.stream()
                .map(questionMapper::toQuestionResponse) // Giả sử bạn có một questionMapper
                .collect(Collectors.toList());

        // Tạo một ExamResponse mới
        ExamResponse examResponse = examMapper.toExamResponse(exam);
        examResponse.setQuestions(questionResponses); // Thêm danh sách câu hỏi vào ExamResponse

        return examResponse;
    }

    public ExamResponse updateExamById(int examId, ExamRequest request) throws IOException{
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_EXISTED));

        String relativeImagePath = FileUtil.saveImage(request.getExamImage(),UPLOAD_DIR);
        exam.setExamImage(relativeImagePath);

        // Kiểm tra các câu hỏi mới có hợp lệ hay không
        List<Question> selectedQuestions = questionRepository.findAllById(request.getQuestionIds());
        for (Question question : selectedQuestions) {
            if (question.getQuestionBank() == null ||
                    !exam.getQuestions().stream().map(Question::getQuestionBank).collect(Collectors.toSet()).contains(question.getQuestionBank())) {
                throw new AppException(ErrorCode.SELECTED_QUESTIONS_INVALID);
            }
        }

        exam.setQuestions(selectedQuestions);

        return examMapper.toExamResponse(examRepository.save(exam));
    }

    public void deleteExamById(int examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_EXISTED));
        examRepository.delete(exam);
    }
}
