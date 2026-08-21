package com.fawwaz_bank.bill_splitter.controller;

import com.fawwaz_bank.bill_splitter.dto.CreateUserRequest;
import com.fawwaz_bank.bill_splitter.model.User;
import com.fawwaz_bank.bill_splitter.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User addUser(
            @RequestBody CreateUserRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());

        return userService.createUser(user);
    }
}