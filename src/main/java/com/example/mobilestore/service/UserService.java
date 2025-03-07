package com.example.mobilestore.service;

import com.example.mobilestore.dto.RegisterDTO;
import com.example.mobilestore.entity.Role;
import com.example.mobilestore.entity.User;
import com.example.mobilestore.exception.AlreadyExistsException;
import com.example.mobilestore.exception.NotFoundException;
import com.example.mobilestore.mapper.UserMapper;
import com.example.mobilestore.repository.RoleRepository;
import com.example.mobilestore.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }

//    public RegisterDTO saveUser(RegisterDTO registerDTO) {
//        if(userRepository.existsByUsername(registerDTO.getUsername())){
//            throw new AlreadyExistsException("User","userName",registerDTO.getUsername());
//        }
//
//        Role role = roleRepository.findById(registerDTO.getRole()).orElseThrow(()-> new
//                NotFoundException(registerDTO.getRole()));
//
//        User user = userMapper.toEntity(registerDTO);
//        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
//        user.setRole(role);
//
//        return userMapper.toDTO(userRepository.save(user));
//    }
//
    public Optional<User> getUserByUserName(String name) {
        return userRepository.findByUsername(name);
    }
}
