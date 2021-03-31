package org.gox.crud.app.controller;

import org.gox.crud.app.entity.User;
import org.gox.crud.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> get(){
        List<User> list = new ArrayList<>();
        userRepository.findAll().forEach(list::add);
        return list;
    }

    @PostMapping
    public User create(@RequestBody User user){
        return userRepository.save(user);
    }
}
