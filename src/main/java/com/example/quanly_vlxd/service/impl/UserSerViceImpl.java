package com.example.quanly_vlxd.service.impl;

import com.example.quanly_vlxd.dto.request.UserRequest;
import com.example.quanly_vlxd.dto.request.ChangePasswordRequest;
import com.example.quanly_vlxd.dto.request.LoginRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.TokenResponse;
import com.example.quanly_vlxd.dto.response.UserResponse;
import com.example.quanly_vlxd.entity.Employee;
import com.example.quanly_vlxd.entity.RefreshToken;
import com.example.quanly_vlxd.entity.Role;
import com.example.quanly_vlxd.entity.User;
import com.example.quanly_vlxd.jwt.JwtTokenProvider;
import com.example.quanly_vlxd.model.UserCustomDetail;
import com.example.quanly_vlxd.repo.EmployeeRepo;
import com.example.quanly_vlxd.repo.RefreshTokenRepo;
import com.example.quanly_vlxd.repo.RoleRepo;
import com.example.quanly_vlxd.repo.UserRepo;
import com.example.quanly_vlxd.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSerViceImpl implements UserService {
    private final UserRepo userRepo;
    private final EmployeeRepo employeeRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepo refreshTokenRepo;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleRepo roleRepo;
    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        Optional<User> user = userRepo.findByUserName(loginRequest.getUsername());
        if(user.isEmpty()){
            return null;
        }
        User userCurrent= user.get();
        userCurrent.setStatus(true);
        userRepo.save(userCurrent);
        String jwtToken= jwtTokenProvider.generateToken(new UserCustomDetail(userCurrent));
        String jwtRefreshToken= jwtTokenProvider.generateRefreshToken(new UserCustomDetail(userCurrent));

        refreshTokenRepo.deleteByUser(userCurrent);
        RefreshToken refreshToken= new RefreshToken();
        refreshToken.setUser(userCurrent);
        refreshToken.setToken(jwtRefreshToken);
        refreshToken.setExpiredTime(jwtTokenProvider.extractExpiration(jwtRefreshToken));
        refreshTokenRepo.save(refreshToken);

        return TokenResponse
                .builder()
                .accessToken(jwtToken)
                .refreshToken(jwtRefreshToken)
                .role(user.get().getRole().getRoleName().name())
                .build();
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        RefreshToken storedToken = refreshTokenRepo.findByToken(refreshToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

        if (storedToken.getExpiredTime().before(new Date())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
        }

        User user = storedToken.getUser();
        String newAccessToken = jwtTokenProvider.generateToken(new UserCustomDetail(user));

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken) // có thể generate mới nếu muốn an toàn hơn
                .role(user.getRole().getRoleName().name())
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> addUser(UserRequest userRequest) {
        boolean isUsernameExist= userRepo.existsByUserName(userRequest.getUsername());
        if(isUsernameExist){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Username already exists!"));
        }
        boolean isPhoneExist= employeeRepo.existsByPhoneNum(userRequest.getPhone());
        if(isPhoneExist){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Phone already exists!"));
        }
        Role role = roleRepo.findByRoleName(userRequest.getRole().name())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        User user= User
                .builder()
                .userName(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .isActive(true)
                .status(false)
                .role(role)
                .build();
        userRepo.save(user);
        Employee manager= new Employee();
        if(userRequest.getManagerId()==0){
            manager=null;
        }else {
            Optional<Employee> emp= employeeRepo.findById(userRequest.getManagerId());
            manager= emp.isEmpty() ? null : emp.get();
        }
        Employee employee = Employee
                .builder()
                .name(userRequest.getFullName())
                .address(userRequest.getAddress())
                .phoneNum(userRequest.getPhone())
                .gender(userRequest.getGender())
                .dob(java.sql.Date.valueOf(userRequest.getDateOfBirth()))
                .manager(manager)
                .isActive(true)
                .user(user)
                .build();
        employeeRepo.save(employee);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MessageResponse("User created successfully!"));
    }

    @Override
    public MessageResponse logout(String username) {
        Optional<User> user= userRepo.findByUserName(username);
        if(user.isEmpty()){
            return MessageResponse.builder().message("Logout Failed!!").build();
        }else {
            user.get().setStatus(false);
            for(RefreshToken refreshToken: refreshTokenRepo.findAll()){
                if(refreshToken.getUser().getId()==user.get().getId()){
                    refreshTokenRepo.delete(refreshToken);
                }
            }
            userRepo.save(user.get());
            return MessageResponse.builder().message("Logout Account "+username+" Successfully!!").build();
        }
    }

    @Override
    public MessageResponse changePass(ChangePasswordRequest changePasswordRequest) {
        Optional<User> user= userRepo.findByUserName(changePasswordRequest.getUsername());
        if (user.isEmpty()){
            return MessageResponse.builder().message("User does not exist!!").build();
        }
        else {
            if(user.get().isStatus() && passwordEncoder.matches(changePasswordRequest.getOldPassword(),user.get().getPassword())){
                if(changePasswordRequest.getNewPassword().equals(changePasswordRequest.getOldPassword())){
                    return MessageResponse.builder().message("new password is the same old password!!!").build();
                }else {
                    user.get().setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
                    user.get().setStatus(false);
                    userRepo.save(user.get());
                    return MessageResponse.builder().message("Change Password Successfully. Please login again!!").build();
                }
            }
        }
        return MessageResponse.builder().message("Change Password Failed!!!").build();
    }

    @Override
    public List<UserResponse> getAllUserActive() {
        List<User> employees = userRepo.findAllEmployee();
        List<UserResponse> empResponse= new ArrayList<>();
        if(!employees.isEmpty()){
            for(User user: employees){
                UserResponse userResponse= UserResponse
                        .builder()
                        .id(user.getId())
                        .userName(user.getUserName())
                        .fullName(user.getEmployee().getName())
                        .phone(user.getEmployee().getPhoneNum())
                        .gender(user.getEmployee().getGender())
                        .status(user.isStatus())
                        .role(user.getRole().getRoleName().name())
                        .address(user.getEmployee().getAddress())
                        .dateOfBirth(user.getEmployee().getDob().toString())
                        .managerId(user.getEmployee().getManager()!= null ? user.getEmployee().getManager().getId():0)
                        .build();
                empResponse.add(userResponse);
            }
        }
        return empResponse;
    }
}
