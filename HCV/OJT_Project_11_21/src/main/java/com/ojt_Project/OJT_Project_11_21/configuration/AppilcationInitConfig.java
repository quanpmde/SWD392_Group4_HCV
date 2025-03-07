package com.ojt_Project.OJT_Project_11_21.configuration;

import com.ojt_Project.OJT_Project_11_21.entity.User;
import com.ojt_Project.OJT_Project_11_21.enums.Role;
import com.ojt_Project.OJT_Project_11_21.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class AppilcationInitConfig {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args ->{
            if (userRepository.findByUserName("admin").isEmpty()){
                User user = User.builder()
                        .userName("admin")
                        .password("admin")
                        .email("admin")
                        .password(passwordEncoder.encode("admin"))
                        .role(Role.ADMIN.name())
                        .isBanned(0)
                        .build();
                userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change it.");
            }
        };
    }
}
