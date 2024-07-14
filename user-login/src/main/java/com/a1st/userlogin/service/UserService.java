package com.a1st.userlogin.service;

import com.a1st.userlogin.dto.RequestResponse;
import com.a1st.userlogin.entity.User;
import com.a1st.userlogin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author: Abderrahman Youabd aka: A1ST
 * @version: 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

  private final UserRepository userRepository;
  private final JwtUtils jwtUtils;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;


  public RequestResponse register(RequestResponse registrationRequest) {
    RequestResponse response = new RequestResponse();

    try {
      User user = new User();
      user.setEmail(registrationRequest.getEmail());
      user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
      user.setCity(registrationRequest.getCity());
      user.setRole(registrationRequest.getRole());
      user.setName(registrationRequest.getName());
      User savedUser = userRepository.save(user);
      if(savedUser.getId() != null && savedUser.getId()>0) {
        response.setUsersList(userRepository.findAll());
        response.setUsers(savedUser);
        response.setMessage("User registered successfully");
      } else {
        response.setMessage("User registration failed");
      }

    } catch(Exception e){
      response.setStatusCode(500);
      response.setError(e.getMessage());
    }
    return response;
  }


  public RequestResponse login(RequestResponse loginRequest) {
    RequestResponse response = new RequestResponse();

    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
      var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
      var jwt = jwtUtils.generateToken(user);
      var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
      response.setToken(jwt);
      response.setStatusCode(200);
      response.setRefreshToken(refreshToken);
      response.setRole(user.getRole());
      response.setExpirationTime("24Hrs");
      response.setMessage("User logged in successfully");

    } catch(Exception e) {
      response.setStatusCode(500);
      response.setError(e.getMessage());
    }
    return response;
  }


  public RequestResponse refreshToken(RequestResponse refreshRequest) {
    RequestResponse response = new RequestResponse();
    try {

      String email = jwtUtils.extractUsername(refreshRequest.getToken());
      User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
      if(jwtUtils.isTokenValid(refreshRequest.getToken(), user)) {
        var jwt = jwtUtils.generateToken(user);
        response.setStatusCode(200);
        response.setToken(jwt);
        response.setRefreshToken(refreshRequest.getToken());
        response.setExpirationTime("24Hrs");
        response.setMessage("User refreshed successfully");
      }

    } catch (Exception e) {
      response.setStatusCode(500);
      response.setError(e.getMessage());
    }
    return response;
  }

  public RequestResponse getAllUsers() {
    RequestResponse response = new RequestResponse();

    try {
      List<User> users = userRepository.findAll();
      if(!users.isEmpty()) {
        response.setStatusCode(200);
        response.setUsersList(users);
        response.setMessage("Users list retrieved successfully");
      } else {
        response.setStatusCode(404);
        response.setError("No Users found");

      }

    } catch (Exception e){
      response.setStatusCode(500);
      response.setError(e.getMessage());
    }
    return response;
  }

  public RequestResponse getUserById(Long id) {
    RequestResponse response = new RequestResponse();

    try {
      List<User> users = userRepository.findAll();
      User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
      response.setStatusCode(200);
      response.setUsers(user);
      response.setMessage("User with id: '" + id+ "' retrieved successfully");
    } catch (Exception e){
      response.setStatusCode(500);
      response.setError(e.getMessage());
    }
    return response;
  }


  public RequestResponse getInfo(String email) {
    RequestResponse response = new RequestResponse();

    try {
      Optional<User> user = userRepository.findByEmail(email);
      if(user.isPresent()) {
        response.setUsers(user.get());
        response.setStatusCode(200);
        response.setMessage("Info retrieved successfully");
      } else {
        response.setStatusCode(404);
        response.setError("No User found");
      }

    } catch (Exception e) {
      response.setStatusCode(500);
      response.setError("error occurred while getting user info: " + e.getMessage());
    }
    return response;
  }




}
