package com.carpoolapp.carpoolService.dto;

import com.carpoolapp.carpoolService.models.Vehicle;
import com.carpoolapp.carpoolService.models.Vehicles;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserInfoDTO {
    private String firstName;
    private String lastName;
    private String emailId;
    private String password;
    private String phoneNumber;
    private String profilePicUrl;
    private String userId;
    private int age;
    private Date dob;
}
