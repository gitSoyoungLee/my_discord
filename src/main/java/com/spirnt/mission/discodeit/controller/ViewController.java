package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.dto.user.UserResponse;
import com.spirnt.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

// 심화 요구사항 구현용
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ViewController {
    private final UserService userService;

    // 모든 사용자 조회 화면 서빙
    @RequestMapping(value = "/users/findAll", method = RequestMethod.GET)
    public String getAllUsersView(Model model) {
        List<UserResponse> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }
}
