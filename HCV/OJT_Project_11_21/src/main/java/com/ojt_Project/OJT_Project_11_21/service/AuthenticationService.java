package com.ojt_Project.OJT_Project_11_21.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.ojt_Project.OJT_Project_11_21.dto.request.AuthenticationRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.AuthenticationResponse;
import com.ojt_Project.OJT_Project_11_21.entity.User;
import com.ojt_Project.OJT_Project_11_21.enums.Role;
import com.ojt_Project.OJT_Project_11_21.exception.AppException;
import com.ojt_Project.OJT_Project_11_21.exception.ErrorCode;
import com.ojt_Project.OJT_Project_11_21.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    protected static final String signature ="OG3aRIYXHjOowyfI2MOHbl8xSjoF/B/XwkK6b276SfXAhL3KbizWWuT8LB1YUVvh";
    @Autowired
    private PasswordEncoder passwordEncoder;
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest){
        User user = userRepository.findByUserEmail(authenticationRequest.getEmail()).orElseThrow(
                () -> new AppException(ErrorCode.EMAIL_NOT_EXISTED)

        );
        if(user.getUserStatus().equalsIgnoreCase("isBanned")){
            throw new AppException(ErrorCode.USER_IS_BANNED);
        }
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getUserPassword());
        if (!authenticated)
            throw new AppException(ErrorCode.LOGIN_FAILED);
        var token =generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true).build();

    }
    public User saveOAuth2User(String email, String name) {
        return userRepository.findByUserEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .userEmail(email)
                    .userName(name)
                    .userStatus("noStatus")
                    .userRole(Role.USER.name())
                    .build();
            return userRepository.save(newUser);
        });
    }

    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserEmail())
                .issuer("Elearning.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("userId",user.getUserId())
                .claim("scope",buildScope(user))
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(signature.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
    public String buildScope(User user) {
        if (!StringUtils.isEmpty(user.getUserRole())) {
            return "" + user.getUserRole();
        }
        return "";

    }
}