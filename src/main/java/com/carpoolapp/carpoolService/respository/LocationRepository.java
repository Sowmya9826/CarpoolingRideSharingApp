package com.carpoolapp.carpoolService.respository;

import com.carpoolapp.carpoolService.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
