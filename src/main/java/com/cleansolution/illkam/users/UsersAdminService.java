package com.cleansolution.illkam.users;

import com.cleansolution.illkam.users.dto.AdminLoginDto;
import com.cleansolution.illkam.users.dto.AdminLoginResponseDto;
import com.cleansolution.illkam.users.dto.UsersResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersAdminService {
    private final UsersRepository usersRepository;

    public Page<Users> getAllUsers(int page){
        Sort sort = Sort.by("createdDateTime").descending();

        Pageable pageable = PageRequest.of(page, 10,sort);
        return usersRepository.findAll(pageable);
    }


    // New Feature 2: Admin login and logout
    public AdminLoginResponseDto login(AdminLoginDto loginDto) {
        // Check if user exists with the provided credentials
        Users user = usersRepository.findByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // Check if the user is an admin
        if (!Boolean.TRUE.equals(user.getIsAdmin())) {
            throw new IllegalArgumentException("Access denied. Admin privileges required.");
        }

        return AdminLoginResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .isAdmin(true)
                .build();
    }

    public void logout(Long adminId) {
        // For now, just validate that the user exists and is an admin
        // In future, you might want to implement token invalidation here
        Users user = usersRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        if (!Boolean.TRUE.equals(user.getIsAdmin())) {
            throw new IllegalArgumentException("Access denied. Admin privileges required.");
        }

        // Log logout action (you can add logging here if needed)
        System.out.println("Admin " + user.getEmail() + " logged out");
    }
}
