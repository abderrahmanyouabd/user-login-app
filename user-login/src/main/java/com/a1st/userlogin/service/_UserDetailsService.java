package com.a1st.userlogin.service;

import com.a1st.userlogin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author: Abderrahman Youabd aka: A1ST
 * @version: 1.0
 */
@Service
@RequiredArgsConstructor
public class _UserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByEmail(username).orElseThrow();
  }
}
