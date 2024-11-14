package com.carpoolapp.carpoolService.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import java.util.Date;

@Entity
@Data
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String emailId;
    private String phoneNumber;
    private String password;
    private int age;
    private Date dob;
    private byte[] profileImage;
}
