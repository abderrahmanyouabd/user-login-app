package com.a1st.userlogin.repository;

import com.a1st.userlogin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author: Abderrahman Youabd aka: A1ST
 * @version: 1.0
 */

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
