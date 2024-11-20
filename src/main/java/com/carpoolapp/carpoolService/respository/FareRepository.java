package com.carpoolapp.carpoolService.respository;

import com.carpoolapp.carpoolService.models.Fare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FareRepository extends JpaRepository<Fare, Long> {

}


