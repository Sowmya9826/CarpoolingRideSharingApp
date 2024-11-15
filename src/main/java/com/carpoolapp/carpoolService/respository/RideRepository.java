package com.carpoolapp.carpoolService.respository;

import com.carpoolapp.carpoolService.models.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long>  {
}




