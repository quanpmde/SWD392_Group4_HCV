    package com.ojt_Project.OJT_Project_11_21.dto.request;

    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.RequiredArgsConstructor;
    import org.springframework.web.multipart.MultipartFile;

    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.Map;

    @Data
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public class ExamRequest {
        private int userId;
        private String userImage;
        private String fullName;
        private String userRole;
        private int subjectId;
        private int examId;
        private String examName;
        private String examPassword;
        private int examTotalQuestions;
        private int examTimer;
        private int examAttempt;
        private String examStatus;
        private int examLikeCount;
        private int examViewCount;
        private int examCommentCount;
        private MultipartFile examImage;
        private LocalDateTime examStartDate;
        private LocalDateTime examEndDate;
        private List<Integer> questionIds;
        private Map<Integer, Integer> randomQuestionBanks;

    }