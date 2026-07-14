package com.ecom.ecom.services;

import com.ecom.ecom.models.User;
import com.ecom.ecom.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public List<User> fetchAllUsers(){
        return userRepository.findAll();

    }

    public Optional<User> fetchUser(Long id){
        return userRepository.findById(id);
    }

    public void addUser(User user){

        userRepository.save(user);

    }




    public boolean updateUser(Long id ,User updateUser) {
        return userRepository.findById(id)
                .map(existingUser ->{
                    existingUser.setFirstName(updateUser.getFirstName());
                    existingUser.setLastName(updateUser.getLastName());
                    userRepository.save(existingUser);
                    return true;
                })
                .orElse(false);


    }
}
