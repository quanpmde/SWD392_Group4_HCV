package com.ojt_Project.OJT_Project_11_21.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.ojt_Project.OJT_Project_11_21.dto.request.AuthenticationRequest;
import com.ojt_Project.OJT_Project_11_21.dto.request.IntrospectRequest;
import com.ojt_Project.OJT_Project_11_21.dto.request.LogOutRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.AuthenticationResponse;
import com.ojt_Project.OJT_Project_11_21.dto.response.IntrospectResponse;
import com.ojt_Project.OJT_Project_11_21.entity.InvalidateToken;
import com.ojt_Project.OJT_Project_11_21.entity.User;
import com.ojt_Project.OJT_Project_11_21.enums.Role;
import com.ojt_Project.OJT_Project_11_21.exception.AppException;
import com.ojt_Project.OJT_Project_11_21.exception.ErrorCode;
import com.ojt_Project.OJT_Project_11_21.repository.InvalidateTokenRepository;
import com.ojt_Project.OJT_Project_11_21.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InvalidateTokenRepository invalidtokenrepository;
    protected static final String signature ="OG3aRIYXHjOowyfI2MOHbl8xSjoF/B/XwkK6b276SfXAhL3KbizWWuT8LB1YUVvh";
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;
    
    
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        User user = userRepository.findByUserEmail(authenticationRequest.getEmail()).orElseThrow(
                () -> new AppException(ErrorCode.EMAIL_NOT_EXISTED)

        );
        if("isBanned".equalsIgnoreCase(user.getUserStatus())){
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
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
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
    
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }
    
    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(signature.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidtokenrepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }
    
    public void logout(LogOutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidateToken invalidatedToken =
            		InvalidateToken.builder().id(jit).expiryTime(expiryTime).build();

            invalidtokenrepository.save(invalidatedToken);
        } catch (AppException exception) {
        	log.warn("Token has already expired or is invalid.");

        }
    }
}