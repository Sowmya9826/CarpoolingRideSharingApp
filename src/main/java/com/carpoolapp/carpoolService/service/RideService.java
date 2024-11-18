package com.carpoolapp.carpoolService.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.time.LocalTime;

@Service
public class RideService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    public LocalTime calculateEndTime(double startLat, double startLng, double destLat, double destLng, LocalTime startTime) {
        try {
            String url = String.format(
                    "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%f,%f&destinations=%f,%f&departure_time=now&key=%s",
                    startLat, startLng, destLat, destLng, apiKey
            );

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

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
}
