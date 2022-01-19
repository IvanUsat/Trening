package com.jm.online_store.controller.rest;

import com.jm.online_store.model.Role;
import com.jm.online_store.model.User;
import com.jm.online_store.service.interf.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
@Component
@RestController
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserRestController {

    private final UserService userService;


    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User saveUser(@RequestBody User user) {
        userService.addUser(user);
        return user;
    }


    @PostMapping("/s")
    public User getUser(){
        return userService.findById(3L).get();

    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        userService.updateUserAdminPanel(user);
        return user;
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId) {
        userService.deleteByID(userId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
