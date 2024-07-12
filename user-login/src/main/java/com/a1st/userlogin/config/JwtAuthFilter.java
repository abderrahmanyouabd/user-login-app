package com.a1st.userlogin.config;

import com.a1st.userlogin.service.JwtUtils;
import com.a1st.userlogin.service._UserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author: Abderrahman Youabd aka: A1ST
 * @version: 1.0
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtUtils jwtUtils;
  private final _UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      final String token = authHeader.substring(7);
      final String email = jwtUtils.extractUsername(token);

      if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if(jwtUtils.isTokenValid(token, userDetails)) {
          SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          securityContext.setAuthentication(authentication);
          SecurityContextHolder.setContext(securityContext);
        }
      }
    } else {
      filterChain.doFilter(request, response);
    }

  }
}
