package com.carpoolapp.carpoolService.respository;

import com.carpoolapp.carpoolService.models.Fare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FareRepository extends JpaRepository<Fare, Long> {

    Optional<Fare> findByRideId(Long id);
}
