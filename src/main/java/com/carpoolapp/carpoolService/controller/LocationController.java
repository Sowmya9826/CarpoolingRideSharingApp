package com.carpoolapp.carpoolService.controller;
import com.carpoolapp.carpoolService.dto.LocationDto;
import com.carpoolapp.carpoolService.models.Location;
import com.carpoolapp.carpoolService.respository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
@RestController
@RequestMapping("/location")
public class LocationController {
    /*
    location/create --POST
    location/{id}. --GET
     */
    @Autowired
    private LocationRepository locationRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createLocation(@RequestBody LocationDto locationDto) {
        Location location = new Location();
        location.setLatitude(locationDto.getLatitude());
        location.setLongitude(locationDto.getLongitude());
        location.setAddress(locationDto.getAddress());

        locationRepository.save(location);
        return ResponseEntity.status(HttpStatus.CREATED).body("Location created successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDto> getLocationById(@PathVariable Long id) {
        Optional<Location> location = locationRepository.findById(id);
        if (location.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            LocationDto locationDto = new LocationDto();
            locationDto.setLatitude(location.get().getLatitude());
            locationDto.setLongitude(location.get().getLongitude());
            locationDto.setAddress(location.get().getAddress());
            return ResponseEntity.ok(locationDto);
        }
    }
}
