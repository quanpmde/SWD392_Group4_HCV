package com.ojt_Project.OJT_Project_11_21.util;

import com.ojt_Project.OJT_Project_11_21.entity.Answer;
import com.ojt_Project.OJT_Project_11_21.entity.Question;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileUtil {
    public static List<Question> readQuestionFromFile(String filePath) throws IOException{
        List<Question> questions = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(filePath)){
            XWPFDocument document = new XWPFDocument(fileInputStream);

            List<XWPFParagraph> paragraphs = document.getParagraphs();
            Question currentQuestion = null;
            List<Answer> currentAnswers = new ArrayList<>();

            for (XWPFParagraph paragraph : paragraphs){
                String text = paragraph.getText().trim();

                //Tìm question mới
                if (text.startsWith("Q:")) {
                    // Lưu câu hỏi trước đó (nếu có)
                    if (currentQuestion != null) {
                        currentQuestion.setAnswers(currentAnswers);
                        questions.add(currentQuestion);
                    }
                    currentQuestion = new Question();
                    currentAnswers = new ArrayList<>();
                } else if (text.startsWith("Description:")) {
                    if (currentQuestion != null) {
                        currentQuestion.setQuestionDescription(text.replace("Description:", "").trim());
                    }
                } else if (text.startsWith("Image:")) {
                    if (currentQuestion != null) {
                        currentQuestion.setQuestionImage(text.replace("Image:", "").trim());
                    }
                } else if (text.startsWith("Explain:")) {
                    if (currentQuestion != null) {
                        currentQuestion.setQuestionAnswerExplain(text.replace("Explain:", "").trim());
                    }
                } else if (text.startsWith("A:")) {
                    Answer answer = new Answer();
                    answer.setAnswerDescription(text); // A: là chỉ định bắt đầu của một câu trả lời
                    currentAnswers.add(answer);
                } else if (text.startsWith("A_Description:")) {
                    if (!currentAnswers.isEmpty()) {
                        currentAnswers.get(currentAnswers.size() - 1).setAnswerDescription(text.replace("A_Description:", "").trim());
                    }
                } else if (text.startsWith("A_Correct:")) {
                    if (!currentAnswers.isEmpty()) {
                        String correctValue = text.replace("A_Correct:", "").trim();
                        currentAnswers.get(currentAnswers.size() - 1).setAnswerCorrect(Integer.parseInt(correctValue));
                    }
                } else if (text.startsWith("A_Image:")) {
                    if (!currentAnswers.isEmpty()) {
                        currentAnswers.get(currentAnswers.size() - 1).setAnswerImage(text.replace("A_Image:", "").trim());
                    }
                }
            }

            // Lưu câu hỏi cuối cùng (nếu có)
            if (currentQuestion != null) {
                currentQuestion.setAnswers(currentAnswers);
                questions.add(currentQuestion);
            }
        }

        return questions;
    }

    public static String saveImage(MultipartFile image, String UPLOAD_DIR) throws IOException {
        if (image != null && !image.isEmpty()) {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String imagePath = UPLOAD_DIR + image.getOriginalFilename();
            Files.write(Paths.get(imagePath), image.getBytes());
            return "img/" + image.getOriginalFilename();
        }
        return null;
    }
}
