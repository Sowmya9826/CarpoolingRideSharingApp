package com.carpoolapp.carpoolService.respository;

import com.carpoolapp.carpoolService.models.Fare;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FareRepository extends JpaRepository<Fare, String> {

}
