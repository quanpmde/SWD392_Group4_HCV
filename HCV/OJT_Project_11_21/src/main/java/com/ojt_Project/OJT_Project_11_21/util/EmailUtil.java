package com.ojt_Project.OJT_Project_11_21.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
    @Autowired
    private JavaMailSender javaMailSender;
    public void sendOtpEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom("tomquan10@gmail.com");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify OTP");
//        mimeMessageHelper.setText("""
//                <div>
//                    <a href = "http://localhost:8080/verify-account?email=%s&otp=%s" target="_blank">Click me to verify</a>
//                </div>
//                """.formatted(email, otp), true);
        mimeMessageHelper.setText("""
                <div>
                    Your OTP is: <strong>%s</strong>
                </div>
                """.formatted(otp), true);
        javaMailSender.send(mimeMessage);
    }

    public void sendActiveAccount(String email, String password) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom("tomquan10@gmail.com");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Active account");
        mimeMessageHelper.setText(String.format("""
                <div>
                    Elearning</br>
                    Your account has been activated.  Your Password: <strong>%s</strong>

             
                    Thank you
                    
                </div>
                """, password), true);
        javaMailSender.send(mimeMessage);
    }

}
