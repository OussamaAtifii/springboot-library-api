package com.atifi.library.auth.controller;

import com.atifi.library.auth.dto.request.LoginRequest;
import com.atifi.library.auth.dto.response.LoginResponse;
import com.atifi.library.auth.service.JwtService;
import com.atifi.library.constants.ApiConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.AUTH_BASE)
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
        );

        Authentication authenticated = authenticationManager.authenticate(authentication);

        System.out.println(authenticated.getName());
        System.out.println(authenticated.getAuthorities());
        System.out.println(authenticated.isAuthenticated());

        String token = jwtService.generateToken(authenticated);

        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(token));
    }

}
