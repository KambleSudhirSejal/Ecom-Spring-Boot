package com.ecom.ecom.dto;

import com.ecom.ecom.models.UserRole;
import lombok.Data;

@Data
public class UserRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    private UserRole role;

    private AddressDTO address;
}
