package com.ak.authservice.controller;

import com.ak.authservice.dto.LoginRequestDto;
import com.ak.authservice.dto.LoginResponseDto;
import com.ak.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthController {

  private static final Logger log = LoggerFactory.getLogger(AuthController.class);
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @Operation(summary = "Generate token for user login")
  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
    Optional<String> tokenOptional = authService.authenticate(loginRequestDto);

    if (tokenOptional.isEmpty()) {
      log.error("Invalid user login");
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    String token = tokenOptional.get();
    return new ResponseEntity<>(new LoginResponseDto(token), HttpStatus.OK);
  }

  @Operation(summary = "Validate Token")
  @GetMapping("/validate")
  public ResponseEntity<LoginResponseDto> validate(@RequestHeader("Authorization") String authHeader) {
    if(StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    return authService.validateToken(authHeader.substring(7))
        ? ResponseEntity.ok().build()
        :ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}
