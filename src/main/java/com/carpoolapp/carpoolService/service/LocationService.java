package com.carpoolapp.carpoolService.service;

import com.carpoolapp.carpoolService.models.Location;
import com.carpoolapp.carpoolService.respository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public Location createLocation(double latitude, double longitude, String address) {
        Location location = new Location();
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setAddress(address);
        locationRepository.save(location);

        return location;
    }
}
