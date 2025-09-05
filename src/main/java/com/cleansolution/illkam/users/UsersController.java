package com.cleansolution.illkam.users;

import com.cleansolution.illkam.users.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // 허용할 Origin 지정
public class UsersController {

    private final UsersService usersService;
    private final UsersAdminService usersAdminService;
    private final UsersRepository usersRepository;

    @GetMapping("/test")
    public String hello(){
        return "hello";
    }

    @PostMapping("/login")
    public Long login(@RequestBody UserLoginDto dto){
        return usersService.login(dto);
    }

    @PostMapping("/")
    public Long save(@RequestBody UsersSaveRequestDto requestDto){
        return usersService.save(requestDto);
    }

    @PostMapping("/{id}/edit")
    public Long edit(@PathVariable("id") Long id, @RequestBody UsersSaveRequestDto requestDto){
        return usersService.edit(id, requestDto);
    }

    @PostMapping("/{id}/fcm")
    public Long updateFcm(@PathVariable("id") Long id, @RequestBody UsersSaveRequestDto requestDto){
        return usersService.updateFcm(id, requestDto);
    }

    @GetMapping("/{id}")
    public UsersResponseDto findById(@PathVariable("id") Long id){
        return usersService.findById(id);
    }

    @GetMapping("/{id}/applier")
    public ApplierResponseDto findApplierById(@PathVariable("id") Long id){
        return usersService.findApplierById(id);
    }
    @GetMapping("/{id}/employer")
    public EmployerResponseDto findEmployerById(@PathVariable("id") Long id){
        return usersService.findEmployerById(id);
    }

//    관리자 페이지
    @GetMapping()
    public Page<Users> getAllUsers(@RequestParam int page){
        return usersAdminService.getAllUsers(page);
    }

//    유저 삭제
    @DeleteMapping("/{id}")
    public Boolean deleteUser(@PathVariable Long id){
        Users user = usersRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("유저가 없습니다."));
        usersRepository.delete(user);
        return true;
    }

//    유저 유료회원 설정
    @PutMapping("/{id}/paid")
    public void changeCashUser(@PathVariable Long id,@RequestBody  PaidPeriodDto dto){
        Users user = usersRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("유저가 없습니다."));
        user.setApprove(dto);
        usersRepository.save(user);
    }


    // New Update: Privacy Policy Feature
    @PutMapping("/{id}/marketing-consent")
    public void updateMarketingConsent(@PathVariable Long id, @RequestBody MarketingConsentDto dto) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
        user.setMarketingConsent(dto.getMarketingConsent());
        usersRepository.save(user);
    }

    @GetMapping("/{id}/marketing-consent")
    public MarketingConsentDto getMarketingConsent(@PathVariable Long id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));

        MarketingConsentDto response = new MarketingConsentDto();
        response.setMarketingConsent(user.getMarketingConsent());
        return response;
    }


    // New Feature 2: Admin login and logout
    // You can add more admin-specific endpoints here
    @GetMapping("/admin-test")
    public String adminTest() {
        return "Admin endpoint working";
    }

    @PostMapping("/admin-login")
    public AdminLoginResponseDto login(@RequestBody AdminLoginDto loginDto) {
        return usersAdminService.login(loginDto);
    }

    @PostMapping("/admin-logout/{id}")
    public String logout(@PathVariable Long id) {
        usersAdminService.logout(id);
        return "Logged out successfully";
    }

}
