package com.ojt_Project.OJT_Project_11_21.repository;

import com.ojt_Project.OJT_Project_11_21.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUserEmail(String userEmail);

    Optional<User> findByUserEmail(String userEmail);

    Optional<User> findByUserName(String userName);
    
    Optional<User> findByUserId(int userId);

    List<User> findByUserRole(String userRole);
    
    long countByUserRole(String userRole);
    
}
