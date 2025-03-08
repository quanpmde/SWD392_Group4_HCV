package com.ojt_Project.OJT_Project_11_21.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "User", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "voucherExchanges"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    int userId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Exam> exams;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<QuestionBank> questionBanks;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Attempt> attempts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Payment> payments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Message> messages;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notification> notifications ;

    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Class> classes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Task> tasks ;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts ;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments ;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Report> reports ;

    @Column(name = "user_email")
    String userEmail;

    @Column(name = "user_password")
    String userPassword;

    @Column(name = "user_name")
    String userName;

    @Column(name = "user_role")
    String userRole;

    @Column(name = "user_phone")
    String userPhone;

    @Column(name = "user_dob")
    String userDob;

    @Column(name = "user_status")
    String userStatus ;

    @Column(name = "user_image")
    String userImage;

    @Column(name = "user_vip")
    int userVip ;

    @Column(name = "user_balance")
    int userBalance;

    @Column(name = "user_message")
    String userMessage;

    @Column(name = "otp", length = 10)
    String otp;

    @Column(name = "generateotptime")
    LocalDateTime generateOtpTime;
}
