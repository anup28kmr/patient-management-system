package com.ak.authservice.service;

import com.ak.authservice.dto.LoginRequestDto;
import com.ak.authservice.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

  public Optional<String> authenticate(LoginRequestDto loginRequestDto) {

    return userService.findByEmail(loginRequestDto.getEmail())
        .filter(user -> passwordEncoder.matches(loginRequestDto.getPassword(),user.getPassword()))
        .map(user->jwtUtil.generateToken(user.getEmail(), user.getRole()));
  }

  public boolean validateToken(String token) {
    try{
      jwtUtil.validateToken(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }
}
