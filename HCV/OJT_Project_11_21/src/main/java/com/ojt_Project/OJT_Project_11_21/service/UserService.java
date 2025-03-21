package com.ojt_Project.OJT_Project_11_21.service;

import com.nimbusds.openid.connect.sdk.UserInfoRequest;
import com.ojt_Project.OJT_Project_11_21.dto.request.UserRegisterRequest;
import com.ojt_Project.OJT_Project_11_21.dto.request.UserUpdateRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.UserMe;
import com.ojt_Project.OJT_Project_11_21.dto.response.UserResponse;
import com.ojt_Project.OJT_Project_11_21.entity.User;
import com.ojt_Project.OJT_Project_11_21.enums.Role;
import com.ojt_Project.OJT_Project_11_21.exception.AppException;
import com.ojt_Project.OJT_Project_11_21.exception.ErrorCode;
import com.ojt_Project.OJT_Project_11_21.mapper.UserMapper;
import com.ojt_Project.OJT_Project_11_21.repository.UserRepository;
import com.ojt_Project.OJT_Project_11_21.util.EmailUtil;
import com.ojt_Project.OJT_Project_11_21.util.FileUtil;
import com.ojt_Project.OJT_Project_11_21.util.OtpUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@Service
public class UserService{
    private static final String UPLOAD_DIR ="C:\\Users\\Admin\\Downloads\\OJT_Project_11_21\\img\\";
    @Autowired
    private OtpUtil otpUtil;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String createNewUser(UserRegisterRequest request)throws IOException {
        if (userRepository.existsByUserEmail(request.getUserEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

//        String otp = otpUtil.generateOtp();
//        try {
//            emailUtil.sendOtpEmail(request.getEmail(), otp);
//        } catch (MessagingException e) {
//            throw new RuntimeException("Unable to send otp please try again");
//        }

        User user = userMapper.toUser(request);
        user.setUserPassword(passwordEncoder.encode(request.getUserPassword()));
        user.setUserRole(Role.USER.name());
        user.setUserStatus("noStatus");
//        user.setOtp(otp);
        user.setGenerateOtpTime(LocalDateTime.now());

        userRepository.save(user);
        return "To verify this is your email account, we will send a confirmation code to this email. Please check your email to receive the verification code to activate your account";
    }

    public User createUser(UserRegisterRequest request){
        if (userRepository.existsByUserEmail(request.getUserEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        User user = userMapper.toUser(request);

        return userRepository.save(user);
    }
    public List<UserResponse> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public UserResponse getUserByEmail(String email){
        return userMapper.toUserResponse(
                userRepository.findByUserEmail(email)
                        .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED))
        );
    }

    public UserResponse updateUserByEmail(String email, UserUpdateRequest request) throws IOException{
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));

        String relativeImagePath = FileUtil.saveImage(request.getUserImage(),UPLOAD_DIR);
        user.setUserImage(relativeImagePath);

        userMapper.updateUser(user,request);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
    
    public UserMe getMyInfomation() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        log.info(name);
        User user = userRepository.findByUserEmail(name).orElseThrow(() -> new AppException(ErrorCode.USER_IS_NOT_FOUNDED));

        return userMapper.toUserMe(user);
    }
}
