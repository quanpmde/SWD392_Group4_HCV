package com.ojt_Project.OJT_Project_11_21.service;

import com.ojt_Project.OJT_Project_11_21.dto.request.TestRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.TestResponse;
import com.ojt_Project.OJT_Project_11_21.dto.response.UserTestResponse;
import com.ojt_Project.OJT_Project_11_21.entity.*;
import com.ojt_Project.OJT_Project_11_21.exception.AppException;
import com.ojt_Project.OJT_Project_11_21.exception.ErrorCode;
import com.ojt_Project.OJT_Project_11_21.mapper.TestMapper;
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
public class TestService {
    @Autowired
    private TestMapper testMapper;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Transactional
    public TestResponse createNewTest(TestRequest request) throws IOException {
        // Tìm user theo userId
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_IS_NOT_FOUNDED));

        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_EXISTED));

        // Kiểm tra số lần làm bài tối đa cho phép
        int examAttemptLimit = exam.getExamAttempt();

        // Lấy bài Test gần nhất của user với exam hiện tại (nếu có)
        Test lastTest = testRepository.findTopByUserAndExamOrderByTestStartDateDesc(user, exam).orElse(null);

        // Tính toán testAttempt: nếu có bài test gần nhất thì lấy testAttempt + 1, nếu không thì đặt là 1
        int testAttempt = (lastTest != null) ? lastTest.getTestAttempt() + 1 : 1;

        // Kiểm tra nếu số lần làm bài đã đạt giới hạn
        if (testAttempt > examAttemptLimit) {
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

        // Tạo mới bài test
        Test test = testMapper.toTest(request);
        test.setUser(user);
        test.setExam(exam);
        test.setQuestions(questions);
        test.setAnswers(answers);
        test.setTestStartDate(LocalDateTime.now());

        // Tăng số lần làm bài (testAttempt) và kiểm tra trạng thái
        test.setTestAttempt(testAttempt + 1);  // Tăng testAttempt

        // Kiểm tra nếu testAttempt đã vượt qua examAttempt
        if (test.getTestAttempt() > examAttemptLimit) {
            test.setTestStatus("over_attempt");
        } else {
            // Tính toán testEndDate dựa trên examTimer
            int examTimer = exam.getExamTimer();  // Thời gian làm bài thi (ví dụ: tính bằng phút)
            test.setTestEndDate(test.getTestStartDate().plusMinutes(examTimer));

            // Kiểm tra trạng thái bài test
            if (LocalDateTime.now().isAfter(test.getTestEndDate())) {
                test.setTestStatus("submitted");
            } else {
                test.setTestStatus("in_progress");
            }
        }

        // Lưu bài test và trả về response
        TestResponse testResponse = testMapper.toTestResponse(testRepository.save(test));
        testResponse.setUserId(user.getUserId());
        testResponse.setExamId(exam.getExamId());
        return testResponse;
    }

    public List<TestResponse> getAllTests() {
        List<Test> tests = testRepository.findAll();
        return tests.stream()
                .map(testMapper::toTestResponse)
                .toList();
    }

    public TestResponse getTestById(int testId) {
        // Tìm bài test theo testId
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new AppException(ErrorCode.TEST_NOT_EXISTED));

        // Chuyển đổi đối tượng Test thành TestResponse
        return testMapper.toTestResponse(test);
    }

    public List<TestResponse> getTestByUserId(int userId){
        List<Test> tests = testRepository.findAllByUser_UserId(userId);
        return tests.stream()
                .map(testMapper::toTestResponse)
                .toList();
    }

    public List<TestResponse> getTestsByUserIdAndExamId(int userId, int examId) {
        // Tìm tất cả các bài thi theo userId và examId
        List<Test> tests = testRepository.findAllByUser_UserIdAndExam_ExamId(userId, examId);

        if (tests.isEmpty()) {
            throw new AppException(ErrorCode.TEST_NOT_EXISTED);
        }

        // Sắp xếp danh sách theo `testStartDate` giảm dần (mới nhất ở đầu)
        List<Test> sortedTests = tests.stream()
                .sorted((t1, t2) -> t2.getTestStartDate().compareTo(t1.getTestStartDate())) // Sắp xếp giảm dần
                .collect(Collectors.toList());

        return sortedTests.stream()
                .map(testMapper::toTestResponse)
                .collect(Collectors.toList());
    }

    public List<TestResponse> getTopTestsByExamId(int examId) {
        List<Test> tests = testRepository.findAllByExam_ExamIdAndTestStatus(examId, "submitted");

        return tests.stream()
                .sorted(Comparator.comparingDouble(Test::getTestPoint).reversed()
                        .thenComparing(Test::getTestTimeSubmitted)) // Sắp xếp theo testPoint giảm dần và thời gian nộp bài
                .map(testMapper::toTestResponse)
                .collect(Collectors.toList());
    }

    public List<UserTestResponse> getTopUsersByExamId(int examId) {
        List<Test> tests = testRepository.findAllByExam_ExamIdAndTestStatus(examId, "submitted");

        // Tạo một map để lưu trữ bài test cao điểm nhất của mỗi user
        Map<Integer, Test> topTestsByUser = new HashMap<>();

        for (Test test : tests) {
            int userId = test.getUser().getUserId();

            // Nếu user chưa có test nào trong map hoặc bài test hiện tại có điểm cao hơn
            if (!topTestsByUser.containsKey(userId) || test.getTestPoint() > topTestsByUser.get(userId).getTestPoint()) {
                topTestsByUser.put(userId, test);
            }
        }

        // Chuyển đổi từ map sang list và sắp xếp theo testPoint giảm dần
        return topTestsByUser.values().stream()
                .sorted(Comparator.comparingDouble(Test::getTestPoint).reversed()
                        .thenComparing(Test::getTestTimeSubmitted)) // Sắp xếp theo testPoint giảm dần và thời gian nộp bài
                .map(test -> {
                    UserTestResponse response = new UserTestResponse();
                    response.setUserId(test.getUser().getUserId());
                    response.setExamId(test.getExam().getExamId());
                    response.setTestPoint(test.getTestPoint());
                    response.setTestTimeSubmitted(test.getTestTimeSubmitted());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public TestResponse updateTest(int testId, TestRequest request) throws IOException{
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new AppException(ErrorCode.TEST_NOT_EXISTED));

        // Kiểm tra trạng thái bài thi, nếu là "submitted" thì không cho phép cập nhật
        if ("submitted".equals(test.getTestStatus())) {
            throw new AppException(ErrorCode.TEST_ALREADY_SUBMITTED);
        }

        List<Integer> questionIds = request.getQuestionIds() != null ? request.getQuestionIds() : new ArrayList<>();
        List<Integer> answerIds = request.getAnswerIds() != null ? request.getAnswerIds() : new ArrayList<>();

        // Lấy danh sách các câu hỏi và câu trả lời nếu có
        if (request.getQuestionIds() != null && !request.getQuestionIds().isEmpty()) {
            List<Question> questions = request.getQuestionIds().stream()
                    .map(questionId -> questionRepository.findById(questionId)
                            .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_EXISTED)))
                    .collect(Collectors.toList());
            test.setQuestions(questions); // Cập nhật danh sách câu hỏi
        }

        if (request.getAnswerIds() != null && !request.getAnswerIds().isEmpty()) {
            List<Answer> answers = request.getAnswerIds().stream()
                    .map(answerId -> answerRepository.findById(answerId)
                            .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_EXISTED)))
                    .collect(Collectors.toList());
            test.setAnswers(answers); // Cập nhật danh sách câu trả lời
        }

        // Kiểm tra trạng thái bài test
        if (LocalDateTime.now().isAfter(test.getTestEndDate())) {
            test.setTestStatus("submitted");
        } else {
            test.setTestStatus("in_progress");
        }

        // Lưu lại bài test đã cập nhật
        TestResponse testResponse = testMapper.toTestResponse(testRepository.save(test));
        return testResponse;
    }

    public TestResponse saveTest(int testId, TestRequest request){
        // Tìm test theo testId
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new AppException(ErrorCode.TEST_NOT_EXISTED));

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

        // Cập nhật danh sách câu trả lời đã chọn cho bài test
        test.setAnswers(selectedAnswers);

        // Lưu lại bài test mà không thay đổi testAttempt và không tính điểm
        TestResponse testResponse = testMapper.toTestResponse(testRepository.save(test));
        testResponse.setUserId(test.getUser().getUserId());
        testResponse.setExamId(test.getExam().getExamId());

        return testResponse;
    }
    @Transactional
    @Scheduled(fixedRate = 1000) // Kiểm tra mỗi giây
    public void autoSubmitTests() {
        LocalDateTime now = LocalDateTime.now();

        // Lấy danh sách các bài test đang hoạt động
        List<Test> activeTests = testRepository.findAllByTestStatus("in_progress");

        for (Test test : activeTests) {
            // Nếu thời gian hiện tại đã quá thời gian kết thúc của test
            if (now.isAfter(test.getTestEndDate())) {
                submitTest(test.getTestId()); // Gọi phương thức submitTest
            } else {
                long secondsLeft = ChronoUnit.SECONDS.between(now, test.getTestEndDate());
                System.out.println("Test ID: " + test.getTestId() + " còn lại: " + secondsLeft + " giây");
            }
        }
    }

    // Phương thức để lấy thời gian còn lại
    public long getTimeLeft(int testId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new AppException(ErrorCode.TEST_NOT_EXISTED));

        return ChronoUnit.SECONDS.between(LocalDateTime.now(), test.getTestEndDate());
    }

    @Transactional
    public TestResponse submitTest(int testId){
        // Tìm test theo testId
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new AppException(ErrorCode.TEST_NOT_EXISTED));

        // Kiểm tra trạng thái bài thi
        if (!"in_progress".equals(test.getTestStatus())) {
            throw new AppException(ErrorCode.TEST_ALREADY_SUBMITTED);
        }

        // Tìm exam liên quan đến bài test
        Exam exam = test.getExam();

        // Khởi tạo biến đếm số câu trả lời đúng
        int correctAnswerCount = 0;

        // Duyệt qua từng câu hỏi trong exam để kiểm tra câu trả lời của bài thi
        for (Question examQuestion : exam.getQuestions()){
            // Lấy danh sách các câu trả lời đúng cho câu hỏi (đáp án đúng của câu hỏi)
            List<Answer> correctAnswers = examQuestion.getAnswers().stream()
                    .filter(answer -> answer.getAnswerCorrect() == 1)
                    .collect(Collectors.toList());

            // Lấy danh sách các câu trả lời được chọn trong bài test cho câu hỏi này (đáp án người dùng chọn)
            List<Answer> selectedAnswers = test.getAnswers().stream()
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

        // Cập nhật số câu trả lời đúng cho bài test
        test.setTestCorrectAnswerCount(correctAnswerCount);

        // Tính tổng số câu hỏi của exam
        int totalQuestions = exam.getQuestions().size();

        // Tính testPoint dựa trên số câu đúng
        double testPoint = 0;
        if (totalQuestions > 0) {
            // Tính testPoint dựa trên số câu đúng
            testPoint = ((double) correctAnswerCount / totalQuestions) * 10;
        }
        test.setTestPoint(testPoint);

        // Đặt trạng thái của bài thi là "submitted"
        test.setTestStatus("submitted");

        // Tính toán thời gian nộp bài
        Duration duration = Duration.between(test.getTestStartDate(), LocalDateTime.now());
        test.setTestTimeSubmitted(TimeUtil.formatDuration(duration));

        // Lưu lại bài test sau khi nộp
        TestResponse testResponse = testMapper.toTestResponse(testRepository.save(test));
        testResponse.setUserId(test.getUser().getUserId());
        testResponse.setExamId(test.getExam().getExamId());
        testResponse.setTestCorrectAnswerCount(correctAnswerCount); // Trả về số câu đúng trong response
        testResponse.setTestPoint(testPoint);

        return testResponse;
    }

}
