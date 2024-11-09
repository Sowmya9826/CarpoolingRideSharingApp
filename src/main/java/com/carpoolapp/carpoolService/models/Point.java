package com.carpoolapp.carpoolService.models;

import lombok.Data;
import lombok.Getter;

@Data
public class Point {
    private double latitude;
    private double longitude;
    private String placeId = "";
    @Getter
    private String placeAddress = null;

    @Override
    public String toString() {
        return "Points{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", placeId='" + placeId + '\'' +
                ", placeAddress='" + placeAddress + '\'' +
                '}';
    }
}
