package com.carpoolapp.carpoolService.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String emailId;
    private String phoneNumber;
    private String password;
    private Date dob;

    @Lob
    private byte[] profileImage;
}
