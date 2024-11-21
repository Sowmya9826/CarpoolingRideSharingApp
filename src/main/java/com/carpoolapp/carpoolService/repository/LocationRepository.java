package com.carpoolapp.carpoolService.repository;

import com.carpoolapp.carpoolService.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
