package com.carpoolapp.carpoolService.models;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Date;

public class UserTest {

    @Test
    public void testUserEntity() {
        // Create a User instance
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmailId("johndoe@example.com");
        user.setPhoneNumber("+1234567890");
        user.setPassword("password123");
        user.setDob(new Date(1990 - 1900, 4, 15));  // Using Date constructor: Year - 1900
        user.setProfileImage(new byte[] {1, 2, 3, 4});

        // Assertions
        assertThat(user).isNotNull();
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getEmailId()).isEqualTo("johndoe@example.com");
        assertThat(user.getPhoneNumber()).isEqualTo("+1234567890");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getDob()).isEqualTo(new Date(1990 - 1900, 4, 15));
        assertThat(user.getProfileImage()).isEqualTo(new byte[] {1, 2, 3, 4});
    }

    @Test
    public void testUserEmptyProfileImage() {
        // Test with empty profileImage (null)
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setEmailId("janedoe@example.com");
        user.setProfileImage(null);  // Empty profile image (null)

        assertThat(user.getProfileImage()).isNull();
    }

    @Test
    public void testUserInvalidPhoneNumber() {
        // Test invalid phone number format
        User user = new User();
        user.setPhoneNumber("invalidPhoneNumber");

        assertThat(user.getPhoneNumber()).isEqualTo("invalidPhoneNumber");  // You could add validation logic here if needed
    }
}
