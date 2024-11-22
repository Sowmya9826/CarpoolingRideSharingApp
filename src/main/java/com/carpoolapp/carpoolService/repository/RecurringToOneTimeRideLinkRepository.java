package com.carpoolapp.carpoolService.repository;

import com.carpoolapp.carpoolService.models.RecurringToOneTimeRideLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecurringToOneTimeRideLinkRepository extends JpaRepository<RecurringToOneTimeRideLink, Long> {
}
