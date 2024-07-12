package com.a1st.userlogin.controller;

import com.a1st.userlogin.dto.RequestResponse;
import com.a1st.userlogin.entity.User;
import com.a1st.userlogin.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Abderrahman Youabd aka: A1ST
 * @version: 1.0
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

  private final UserService userService;

  @PostMapping("/auth/register")
  public ResponseEntity<RequestResponse> register(@RequestBody RequestResponse request) {
    return ResponseEntity.ok(userService.register(request));
  }

  @PostMapping("/auth/login")
  public ResponseEntity<RequestResponse> login(@RequestBody RequestResponse request) {
    return ResponseEntity.ok(userService.login(request));
  }

  @PostMapping("/auth/refresh")
  public ResponseEntity<RequestResponse> refreshToken(@RequestBody RequestResponse request) {
    return ResponseEntity.ok(userService.refreshToken(request));
  }

  @GetMapping("/admin/get-all-users")
  public ResponseEntity<RequestResponse> getAllUsers() {
    log.info("reached");
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @GetMapping("/admin/users/{id}")
  public ResponseEntity<RequestResponse> getUser(@PathVariable Long id) {
    log.info("reached");
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @GetMapping("/user-admin/profile")
  public ResponseEntity<RequestResponse> getUserProfile() {
    RequestResponse response = null;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      String email = authentication.getName();
      response = userService.getInfo(email);
      return ResponseEntity.ok(response);
    }
    return null;
  }


}
