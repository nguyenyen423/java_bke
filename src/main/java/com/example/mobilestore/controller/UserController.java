package com.example.mobilestore.controller;

import com.example.mobilestore.dto.LoginDTO;
import com.example.mobilestore.exception.InvalidCredentialsException;
import com.example.mobilestore.security.JwtTokenProvider;
import com.example.mobilestore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public UserController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }




//    @PostMapping("/register")
//    public ResponseEntity<RegisterDTO> register(@RequestBody RegisterDTO registerDTO) {
//        RegisterDTO user = userService.saveUser(registerDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(user);
//    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO loginDTO){
        try {
            // Xác thực thông tin người dùng bằng AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
            );
            // Nếu xác thực thành công, tạo JWT token
            return ResponseEntity.ok(tokenProvider.generateToken(authentication));
        } catch (AuthenticationException e) {
            // Nếu xác thực thất bại, ném ngoại lệ với thông báo lỗi
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    @GetMapping("/encode-password")
    public String encodePassword(@RequestParam String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
