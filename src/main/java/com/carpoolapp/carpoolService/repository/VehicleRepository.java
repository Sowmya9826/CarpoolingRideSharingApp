package com.carpoolapp.carpoolService.repository;

import com.carpoolapp.carpoolService.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
