package com.cleansolution.illkam.firebase;

import com.cleansolution.illkam.firebase.entity.Notifications;
import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fcm")
@RequiredArgsConstructor
public class FCMController {
    private final FCMService fcmService;
    private  final UsersRepository usersRepository;

    @GetMapping("/test/{id}")
    public Boolean test(@PathVariable Long id) throws Exception {
        Users user = usersRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("test 실패"));
        fcmService.sendMessage(user, "test 전송","ㅁㄴㅇㄹ","work",1L);
        return true;
    }

    @GetMapping("/{id}/user")
    public List<Notifications> getUserAlarms(@PathVariable Long id) throws Exception {
        return fcmService.getUserNotifications(id);
    }

    @PostMapping("/{id}/user")
    public Boolean sendAlarm(@PathVariable Long id,@RequestBody FCMRequestDto dto) throws Exception {
        System.out.println("fcm 요청");
        return fcmService.sendMessage(id,dto);
    }
}
