package com.carpoolapp.carpoolService.models;

import lombok.Data;
import java.util.Date;
import java.util.List;
@Data

public class User {
    private String userId;
    private String fullName;
    private String emailId;
    private String phoneNumber;
    private String password;
    private int age;
    private Date dob;
    private String empId;
    private String profilePicUrl;
    private List<Vehicles> userCars;
    private Date createdDate = new Date();
    private String createdBy = emailId;
}
