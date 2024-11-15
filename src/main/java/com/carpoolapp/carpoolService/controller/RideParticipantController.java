package com.carpoolapp.carpoolService.controller;

import com.carpoolapp.carpoolService.dto.RideParticipantDto;
import com.carpoolapp.carpoolService.models.RideParticipant;
import com.carpoolapp.carpoolService.respository.RideParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rideparticipants")
public class RideParticipantController {

    @Autowired
    private RideParticipantRepository rideParticipantRepository;

    /**
     * Create a new ride participant
     * POST: /rideparticipants/create
     */
    @PostMapping("/create")
    public ResponseEntity<String> createRideParticipant(@RequestBody RideParticipantDto rideParticipantDto) {
        RideParticipant rideParticipant = new RideParticipant();
        rideParticipant.setRide(rideParticipantDto.getRide());
        rideParticipant.setParticipant(rideParticipantDto.getParticipant());
        rideParticipant.setRole(rideParticipantDto.getRole());

        rideParticipantRepository.save(rideParticipant);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ride Participant created successfully");
    }

    /**
     * Get participants of a ride by rideId (returns username and phone number)
     * GET: /rideparticipants/ride/{rideId}
     */
    @GetMapping("/ride/{rideId}")
    public ResponseEntity<List<RideParticipantDto>> getParticipantsByRide(@PathVariable Long rideId) {
        List<RideParticipant> participants = rideParticipantRepository.findByRideId(rideId);

        if (participants.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<RideParticipantDto> participantDtos = participants.stream().map(participant -> {
            RideParticipantDto dto = new RideParticipantDto();
            dto.setRide(participant.getRide());
            dto.setParticipant(participant.getParticipant());
            dto.setRole(participant.getRole());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(participantDtos);
    }

    /**
     * Delete a ride participant by rideParticipantId
     * DELETE: /rideparticipants/delete/{rideParticipantId}
     */
    @DeleteMapping("/delete/{rideParticipantId}")
    public ResponseEntity<String> deleteRideParticipant(@PathVariable Long rideParticipantId) {
        Optional<RideParticipant> rideParticipantOptional = rideParticipantRepository.findById(rideParticipantId);

        if (rideParticipantOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ride Participant not found");
        }

        rideParticipantRepository.delete(rideParticipantOptional.get());
        return ResponseEntity.ok("Ride Participant deleted successfully");
    }
}
