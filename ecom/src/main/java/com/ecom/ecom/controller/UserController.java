package com.ecom.ecom.controller;

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
//    @RequestMapping(value="/api/users" , method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);


    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id){


        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<String> createUsers(@RequestBody User user){
        userService.addUser(user);
        return ResponseEntity.ok("user added successfully");

    }



    @PutMapping("/{id}")
    public ResponseEntity<String> editUser(@PathVariable  Long id, @RequestBody User updatedUser){

       boolean updated =  userService.updateUser(id,updatedUser);
       if(updated){
           return ResponseEntity.ok("user updated successfully");
       }
       return ResponseEntity.notFound().build();

    }


}
