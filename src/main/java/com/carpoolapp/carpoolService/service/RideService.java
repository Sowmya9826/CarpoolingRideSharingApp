package com.carpoolapp.carpoolService.service;

import com.carpoolapp.carpoolService.dto.RideDto;
import com.carpoolapp.carpoolService.models.*;
import com.carpoolapp.carpoolService.models.enums.RideParticipantStatus;
import com.carpoolapp.carpoolService.models.enums.RideParticipateRole;
import com.carpoolapp.carpoolService.models.enums.RideStatus;
import com.carpoolapp.carpoolService.models.enums.RideType;
import com.carpoolapp.carpoolService.respository.LocationRepository;
import com.carpoolapp.carpoolService.respository.RideParticipantRepository;
import com.carpoolapp.carpoolService.respository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@Service
public class RideService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private RideParticipantRepository rideParticipantRepository;

    public LocalTime calculateEndTime(double startLat, double startLng, double destLat, double destLng, LocalTime startTime) {
        try {
            String url = String.format(
                    "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%f,%f&destinations=%f,%f&departure_time=now&key=%s",
                    startLat, startLng, destLat, destLng, apiKey
            );

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            // if the response is null or error, set the end time to 30 minutes after the start time
            if (response == null || response.contains("error_message")) {
                return startTime.plusMinutes(30);
            }

            JSONObject json = new JSONObject(response);
            int durationInSeconds = json
                    .getJSONArray("rows")
                    .getJSONObject(0)
                    .getJSONArray("elements")
                    .getJSONObject(0)
                    .getJSONObject("duration")
                    .getInt("value"); // Duration in seconds

            // Add the duration to the start time
            return startTime.plusSeconds(durationInSeconds);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to calculate end time");
        }
    }


    public Ride createRide(RideDto rideDto, Vehicle vehicle, Location startLocation, Location endLocation) {
        Ride ride = new Ride();

        ride.setVehicle(vehicle);
        ride.setPickupLocation(startLocation);
        ride.setDestinationLocation(endLocation);
        ride.setStatus(RideStatus.CREATED);
        ride.setStartTime(rideDto.getStartTime());
        ride.setEndTime(calculateEndTime(rideDto.getStartLatitude(), rideDto.getStartLongitude(), rideDto.getEndLatitude(), rideDto.getEndLongitude(), rideDto.getStartTime()));
        ride.setAvailableSeats(vehicle.getSeatCount() - 1); // Driver's seat
        ride.setCreatedDate(LocalDateTime.now(ZoneId.of("UTC")).toLocalDate());

        if (rideDto.getDate() != null) {
            ride.setDate(rideDto.getDate());
            ride.setType(RideType.ONE_TIME);
        } else {
            ride.setDaysOfWeek(rideDto.getDaysOfWeek());
            ride.setType(RideType.RECURRING);
        }

        Ride savedRide = rideRepository.save(ride);

        return savedRide;
    }

}
