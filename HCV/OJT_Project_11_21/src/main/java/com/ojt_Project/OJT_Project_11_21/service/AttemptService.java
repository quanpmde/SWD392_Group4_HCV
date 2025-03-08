package com.ojt_Project.OJT_Project_11_21.service;

import com.ojt_Project.OJT_Project_11_21.dto.request.AttemptRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.AttemptResponse;
import com.ojt_Project.OJT_Project_11_21.dto.response.UserAttemptResponse;
import com.ojt_Project.OJT_Project_11_21.entity.*;
import com.ojt_Project.OJT_Project_11_21.exception.AppException;
import com.ojt_Project.OJT_Project_11_21.exception.ErrorCode;
import com.ojt_Project.OJT_Project_11_21.mapper.AttemptMapper;
import com.ojt_Project.OJT_Project_11_21.repository.*;
import com.ojt_Project.OJT_Project_11_21.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttemptService {
    @Autowired
    private AttemptMapper attemptMapper;
    @Autowired
    private AttemptRepository attemptRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Transactional
    public AttemptResponse createNewAttempt(AttemptRequest request) throws IOException {
        // Tìm user theo userId
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_IS_NOT_FOUNDED));

        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_EXISTED));

        // Kiểm tra số lần làm bài tối đa cho phép
        int examAttemptLimit = exam.getExamAttemptCount();

        // Lấy bài Attempt gần nhất của user với exam hiện tại (nếu có)
        Attempt lastAttempt = attemptRepository.findTopByUserAndExamOrderByAttemptStartDateDesc(user, exam).orElse(null);

        // Tính toán attemptAttempt: nếu có bài attempt gần nhất thì lấy attemptAttempt + 1, nếu không thì đặt là 1
        int attemptCount = (lastAttempt != null) ? lastAttempt.getAttemptCount() + 1 : 1;

        // Kiểm tra nếu số lần làm bài đã đạt giới hạn
        if (attemptCount > examAttemptLimit) {
            throw new AppException(ErrorCode.OVER_ATTEMPT_LIMIT);
        }

        // Lấy danh sách các câu hỏi dựa trên danh sách ID
        List<Question> questions = request.getQuestionIds().stream()
                .map(questionId -> questionRepository.findById(questionId)
                        .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_EXISTED)))
                .collect(Collectors.toList());

        // Lấy danh sách câu trả lời dựa trên danh sách ID
        List<Answer> answers = request.getAnswerIds().stream()
                .map(answerId -> answerRepository.findById(answerId)
                        .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_EXISTED)))
                .collect(Collectors.toList());

        // Tạo mới bài attempt
        com.ojt_Project.OJT_Project_11_21.entity.Attempt attempt = attemptMapper.toAttempt(request);
        attempt.setUser(user);
        attempt.setExam(exam);
        attempt.setQuestions(questions);
        attempt.setAnswers(answers);
        attempt.setAttemptStartDate(LocalDateTime.now());

        // Tăng số lần làm bài (attemptAttempt) và kiểm tra trạng thái
        attempt.setAttemptCount(attemptCount + 1);  // Tăng attemptAttempt

        // Kiểm tra nếu attemptAttempt đã vượt qua examAttempt
        if (attempt.getAttemptCount() > examAttemptLimit) {
            attempt.setAttemptStatus("over_attempt");
        } else {
            // Tính toán attemptEndDate dựa trên examTimer
            int examTimer = exam.getExamTimer();  // Thời gian làm bài thi (ví dụ: tính bằng phút)
            attempt.setAttemptEndDate(attempt.getAttemptStartDate().plusMinutes(examTimer));

            // Kiểm tra trạng thái bài attempt
            if (LocalDateTime.now().isAfter(attempt.getAttemptEndDate())) {
                attempt.setAttemptStatus("submitted");
            } else {
                attempt.setAttemptStatus("in_progress");
            }
        }

        // Lưu bài attempt và trả về response
        AttemptResponse attemptResponse = attemptMapper.toAttemptResponse(attemptRepository.save(attempt));
        attemptResponse.setUserId(user.getUserId());
        attemptResponse.setExamId(exam.getExamId());
        return attemptResponse;
    }

    public List<AttemptResponse> getAllAttempts() {
        List<com.ojt_Project.OJT_Project_11_21.entity.Attempt> attempts = attemptRepository.findAll();
        return attempts.stream()
                .map(attemptMapper::toAttemptResponse)
                .toList();
    }

    public AttemptResponse getAttemptById(int attemptId) {
        // Tìm bài attempt theo attemptId
        com.ojt_Project.OJT_Project_11_21.entity.Attempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTEMPT_NOT_EXISTED));

        // Chuyển đổi đối tượng Attempt thành AttemptResponse
        return attemptMapper.toAttemptResponse(attempt);
    }

    public List<AttemptResponse> getAttemptByUserId(int userId){
        List<com.ojt_Project.OJT_Project_11_21.entity.Attempt> attempts = attemptRepository.findAllByUser_UserId(userId);
        return attempts.stream()
                .map(attemptMapper::toAttemptResponse)
                .toList();
    }

    public List<AttemptResponse> getAttemptsByUserIdAndExamId(int userId, int examId) {
        // Tìm tất cả các bài thi theo userId và examId
        List<com.ojt_Project.OJT_Project_11_21.entity.Attempt> attempts = attemptRepository.findAllByUser_UserIdAndExam_ExamId(userId, examId);

        if (attempts.isEmpty()) {
            throw new AppException(ErrorCode.ATTEMPT_NOT_EXISTED);
        }

        // Sắp xếp danh sách theo `attemptStartDate` giảm dần (mới nhất ở đầu)
        List<com.ojt_Project.OJT_Project_11_21.entity.Attempt> sortedAttempts = attempts.stream()
                .sorted((t1, t2) -> t2.getAttemptStartDate().compareTo(t1.getAttemptStartDate())) // Sắp xếp giảm dần
                .collect(Collectors.toList());

        return sortedAttempts.stream()
                .map(attemptMapper::toAttemptResponse)
                .collect(Collectors.toList());
    }

    public List<AttemptResponse> getTopAttemptsByExamId(int examId) {
        List<com.ojt_Project.OJT_Project_11_21.entity.Attempt> attempts = attemptRepository.findAllByExam_ExamIdAndAttemptStatus(examId, "submitted");

        return attempts.stream()
                .sorted(Comparator.comparingDouble(com.ojt_Project.OJT_Project_11_21.entity.Attempt::getAttemptPoint).reversed()
                        .thenComparing(com.ojt_Project.OJT_Project_11_21.entity.Attempt::getAttemptTimeSubmitted)) // Sắp xếp theo attemptPoint giảm dần và thời gian nộp bài
                .map(attemptMapper::toAttemptResponse)
                .collect(Collectors.toList());
    }

    public List<UserAttemptResponse> getTopUsersByExamId(int examId) {
        List<com.ojt_Project.OJT_Project_11_21.entity.Attempt> attempts = attemptRepository.findAllByExam_ExamIdAndAttemptStatus(examId, "submitted");

        // Tạo một map để lưu trữ bài attempt cao điểm nhất của mỗi user
        Map<Integer, com.ojt_Project.OJT_Project_11_21.entity.Attempt> topAttemptsByUser = new HashMap<>();

        for (com.ojt_Project.OJT_Project_11_21.entity.Attempt attempt : attempts) {
            int userId = attempt.getUser().getUserId();

            // Nếu user chưa có attempt nào trong map hoặc bài attempt hiện tại có điểm cao hơn
            if (!topAttemptsByUser.containsKey(userId) || attempt.getAttemptPoint() > topAttemptsByUser.get(userId).getAttemptPoint()) {
                topAttemptsByUser.put(userId, attempt);
            }
        }

        // Chuyển đổi từ map sang list và sắp xếp theo attemptPoint giảm dần
        return topAttemptsByUser.values().stream()
                .sorted(Comparator.comparingDouble(com.ojt_Project.OJT_Project_11_21.entity.Attempt::getAttemptPoint).reversed()
                        .thenComparing(com.ojt_Project.OJT_Project_11_21.entity.Attempt::getAttemptTimeSubmitted)) // Sắp xếp theo attemptPoint giảm dần và thời gian nộp bài
                .map(attempt -> {
                    UserAttemptResponse response = new UserAttemptResponse();
                    response.setUserId(attempt.getUser().getUserId());
                    response.setExamId(attempt.getExam().getExamId());
                    response.setAttemptPoint(attempt.getAttemptPoint());
                    response.setAttemptTimeSubmitted(attempt.getAttemptTimeSubmitted());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public AttemptResponse updateAttempt(int attemptId, AttemptRequest request) throws IOException{
        com.ojt_Project.OJT_Project_11_21.entity.Attempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTEMPT_NOT_EXISTED));

        // Kiểm tra trạng thái bài thi, nếu là "submitted" thì không cho phép cập nhật
        if ("submitted".equals(attempt.getAttemptStatus())) {
            throw new AppException(ErrorCode.ATTEMPT_ALREADY_SUBMITTED);
        }

        List<Integer> questionIds = request.getQuestionIds() != null ? request.getQuestionIds() : new ArrayList<>();
        List<Integer> answerIds = request.getAnswerIds() != null ? request.getAnswerIds() : new ArrayList<>();

        // Lấy danh sách các câu hỏi và câu trả lời nếu có
        if (request.getQuestionIds() != null && !request.getQuestionIds().isEmpty()) {
            List<Question> questions = request.getQuestionIds().stream()
                    .map(questionId -> questionRepository.findById(questionId)
                            .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_EXISTED)))
                    .collect(Collectors.toList());
            attempt.setQuestions(questions); // Cập nhật danh sách câu hỏi
        }

        if (request.getAnswerIds() != null && !request.getAnswerIds().isEmpty()) {
            List<Answer> answers = request.getAnswerIds().stream()
                    .map(answerId -> answerRepository.findById(answerId)
                            .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_EXISTED)))
                    .collect(Collectors.toList());
            attempt.setAnswers(answers); // Cập nhật danh sách câu trả lời
        }

        // Kiểm tra trạng thái bài attempt
        if (LocalDateTime.now().isAfter(attempt.getAttemptEndDate())) {
            attempt.setAttemptStatus("submitted");
        } else {
            attempt.setAttemptStatus("in_progress");
        }

        // Lưu lại bài attempt đã cập nhật
        AttemptResponse attemptResponse = attemptMapper.toAttemptResponse(attemptRepository.save(attempt));
        return attemptResponse;
    }

    public AttemptResponse saveAttempt(int attemptId, AttemptRequest request){
        // Tìm attempt theo attemptId
        com.ojt_Project.OJT_Project_11_21.entity.Attempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTEMPT_NOT_EXISTED));

        // Lấy danh sách câu hỏi từ request
        List<Question> selectedQuestions = request.getQuestionIds().stream()
                .map(questionId -> questionRepository.findById(questionId)
                        .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_EXISTED)))
                .collect(Collectors.toList());

        // Lấy danh sách các câu trả lời mới từ request
        List<Answer> selectedAnswers = request.getAnswerIds().stream()
                .map(answerId -> answerRepository.findById(answerId)
                        .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_EXISTED)))
                .collect(Collectors.toList());

        // Cập nhật danh sách câu trả lời đã chọn cho bài attempt
        attempt.setAnswers(selectedAnswers);

        // Lưu lại bài attempt mà không thay đổi attemptAttempt và không tính điểm
        AttemptResponse attemptResponse = attemptMapper.toAttemptResponse(attemptRepository.save(attempt));
        attemptResponse.setUserId(attempt.getUser().getUserId());
        attemptResponse.setExamId(attempt.getExam().getExamId());

        return attemptResponse;
    }
    @Transactional
    @Scheduled(fixedRate = 1000) // Kiểm tra mỗi giây
    public void autoSubmitAttempts() {
        LocalDateTime now = LocalDateTime.now();

        // Lấy danh sách các bài attempt đang hoạt động
        List<com.ojt_Project.OJT_Project_11_21.entity.Attempt> activeAttempts = attemptRepository.findAllByAttemptStatus("in_progress");

        for (com.ojt_Project.OJT_Project_11_21.entity.Attempt attempt : activeAttempts) {
            // Nếu thời gian hiện tại đã quá thời gian kết thúc của attempt
            if (now.isAfter(attempt.getAttemptEndDate())) {
                submitAttempt(attempt.getAttemptId()); // Gọi phương thức submitAttempt
            } else {
                long secondsLeft = ChronoUnit.SECONDS.between(now, attempt.getAttemptEndDate());
                System.out.println("Attempt ID: " + attempt.getAttemptId() + " còn lại: " + secondsLeft + " giây");
            }
        }
    }

    // Phương thức để lấy thời gian còn lại
    public long getTimeLeft(int attemptId) {
        com.ojt_Project.OJT_Project_11_21.entity.Attempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTEMPT_NOT_EXISTED));

        return ChronoUnit.SECONDS.between(LocalDateTime.now(), attempt.getAttemptEndDate());
    }

    @Transactional
    public AttemptResponse submitAttempt(int attemptId){
        // Tìm attempt theo attemptId
        com.ojt_Project.OJT_Project_11_21.entity.Attempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTEMPT_NOT_EXISTED));

        // Kiểm tra trạng thái bài thi
        if (!"in_progress".equals(attempt.getAttemptStatus())) {
            throw new AppException(ErrorCode.ATTEMPT_ALREADY_SUBMITTED);
        }

        // Tìm exam liên quan đến bài attempt
        Exam exam = attempt.getExam();

        // Khởi tạo biến đếm số câu trả lời đúng
        int correctAnswerCount = 0;

        // Duyệt qua từng câu hỏi trong exam để kiểm tra câu trả lời của bài thi
        for (Question examQuestion : exam.getQuestions()){
            // Lấy danh sách các câu trả lời đúng cho câu hỏi (đáp án đúng của câu hỏi)
            List<Answer> correctAnswers = examQuestion.getAnswers().stream()
                    .filter(answer -> answer.getAnswerCorrect() == 1)
                    .collect(Collectors.toList());

            // Lấy danh sách các câu trả lời được chọn trong bài attempt cho câu hỏi này (đáp án người dùng chọn)
            List<Answer> selectedAnswers = attempt.getAnswers().stream()
                    .filter(answer -> answer.getQuestion().getQuestionId() == examQuestion.getQuestionId())
                    .collect(Collectors.toList());

            // Kiểm tra nếu số lượng câu trả lời được chọn là đúng (trường hợp 2 hoặc nhiều câu trả lời trở lên)
            boolean allSelectedCorrect = selectedAnswers.stream()
                    .allMatch(selectedAnswer -> selectedAnswer.getAnswerCorrect() == 1);


            // Kiểm tra nếu số lượng câu trả lời đúng và câu trả lời được chọn phải bằng nhau
            if (selectedAnswers.size() == correctAnswers.size() && allSelectedCorrect) {
                // Nếu câu trả lời đúng, tăng biến đếm
                correctAnswerCount++;
            }
        }

        // Cập nhật số câu trả lời đúng cho bài attempt
        attempt.setAttemptCorrectAnswerCount(correctAnswerCount);

        // Tính tổng số câu hỏi của exam
        int totalQuestions = exam.getQuestions().size();

        // Tính attemptPoint dựa trên số câu đúng
        double attemptPoint = 0;
        if (totalQuestions > 0) {
            // Tính attemptPoint dựa trên số câu đúng
            attemptPoint = ((double) correctAnswerCount / totalQuestions) * 10;
        }
        attempt.setAttemptPoint(attemptPoint);

        // Đặt trạng thái của bài thi là "submitted"
        attempt.setAttemptStatus("submitted");

        // Tính toán thời gian nộp bài
        Duration duration = Duration.between(attempt.getAttemptStartDate(), LocalDateTime.now());
        attempt.setAttemptTimeSubmitted(TimeUtil.formatDuration(duration));

        // Lưu lại bài attempt sau khi nộp
        AttemptResponse attemptResponse = attemptMapper.toAttemptResponse(attemptRepository.save(attempt));
        attemptResponse.setUserId(attempt.getUser().getUserId());
        attemptResponse.setExamId(attempt.getExam().getExamId());
        attemptResponse.setAttemptCorrectAnswerCount(correctAnswerCount); // Trả về số câu đúng trong response
        attemptResponse.setAttemptPoint(attemptPoint);

        return attemptResponse;
    }

}
