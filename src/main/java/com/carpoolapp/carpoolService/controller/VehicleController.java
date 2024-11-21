package com.carpoolapp.carpoolService.controller;

import com.carpoolapp.carpoolService.dto.VehicleDto;
import com.carpoolapp.carpoolService.models.User;
import com.carpoolapp.carpoolService.models.Vehicle;
import com.carpoolapp.carpoolService.repository.UserRepository;
import com.carpoolapp.carpoolService.repository.VehicleRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/create")
    public String createVehicle(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("userId", userId);

        return "vehicle_pages/new_vehicle_form";
    }

    @PostMapping("/create")
    public String createVehicle(VehicleDto vehicleDto, RedirectAttributes redirectAttributes) {
        Vehicle vehicle = new Vehicle();

        User owner = userRepository.findById(vehicleDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        vehicle.setOwner(owner);
        vehicle.setNumber(vehicleDto.getNumber());
        vehicle.setType(vehicleDto.getType());
        vehicle.setName(vehicleDto.getName());
        vehicle.setColor(vehicleDto.getColor());
        vehicle.setSeatCount(vehicleDto.getSeatCount());

        vehicleRepository.save(vehicle);
        redirectAttributes.addFlashAttribute("message", "Vehicle added successfully!");

        return "redirect:/vehicles/";
    }


    @GetMapping("/")
    public String showVehicles(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }

        List<Vehicle> vehicles = vehicleRepository.findAll().stream()
                .filter(vehicle -> vehicle.getOwner() != null && vehicle.getOwner().getId().equals(userId))
                .collect(Collectors.toList());

        model.addAttribute("vehicles", vehicles);

        return "vehicle_pages/index";
    }

}
