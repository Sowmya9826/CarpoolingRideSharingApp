package com.carpoolapp.carpoolService.respository;

import com.carpoolapp.carpoolService.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // No additional methods needed; findById is provided by JpaRepository
}

