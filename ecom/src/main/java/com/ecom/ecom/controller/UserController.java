package com.ecom.ecom.controller;

import com.ecom.ecom.dto.UserRequest;
import com.ecom.ecom.dto.UserResponse;
import com.ecom.ecom.services.UserService;
import com.ecom.ecom.models.User;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);


    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id){
        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<String> createUsers(@RequestBody UserRequest userRequest){
        userService.addUser(userRequest);
        return ResponseEntity.ok("user added successfully");

    }



    @PutMapping("/{id}")
    public ResponseEntity<String> editUser(@PathVariable  Long id, @RequestBody UserRequest updatedUserRequest){

       boolean updated =  userService.updateUser(id,updatedUserRequest);
       if(updated){
           return ResponseEntity.ok("user updated successfully");
       }
       return ResponseEntity.notFound().build();

    }


}
