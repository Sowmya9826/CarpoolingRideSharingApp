package com.carpoolapp.carpoolService.controller;

import com.carpoolapp.carpoolService.dto.PaymentSummaryDto;
import com.carpoolapp.carpoolService.dto.RideOwedDto;
import com.carpoolapp.carpoolService.dto.RideOwedToUserDto;
import com.carpoolapp.carpoolService.repository.TransactionRepository;
import com.carpoolapp.carpoolService.service.TransactionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/index")
    public String index(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        Double totalOwed = transactionRepository.findTotalOwedByUser(userId, LocalDateTime.now().toLocalDate(),
                LocalDateTime.now().toLocalTime());
        Double totalOwing = transactionRepository.findTotalOwedToUser(userId, LocalDateTime.now().toLocalDate(),
                LocalDateTime.now().toLocalTime());

        PaymentSummaryDto paymentSummaryDto = new PaymentSummaryDto();
        paymentSummaryDto.setTotalOwed(totalOwed != null ? (Math.round(totalOwed * 100.0) / 100.0) : 0.0);
        paymentSummaryDto.setTotalOwing(totalOwing != null ? (Math.round(totalOwing * 100.0) / 100.0) : 0.0);

        model.addAttribute("paymentSummary", paymentSummaryDto);

        return "payment_pages/index";
    }

    @GetMapping("/rides-owed")
    public String showRidesOwed(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }

        List<RideOwedDto> rides = transactionService.getRidesWhereUserOwes(userId);

        // round the amount to 2 decimal places
        rides.forEach(ride -> ride.setAmountOwed(Math.round(ride.getAmountOwed() * 100.0) / 100.0));

        model.addAttribute("rides", rides);

        return "payment_pages/rides_owed";
    }


    @GetMapping("/rides-owed-to-user")
    public String showRidesOwedToUser(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }

        List<RideOwedToUserDto> ridesOwedToUser = transactionRepository.findRidesOwedToUser(userId, LocalDateTime.now().toLocalDate(),
                LocalDateTime.now().toLocalTime());

        // round the amount to 2 decimal places
        ridesOwedToUser.forEach(ride -> ride.setTotalAmountOwed(Math.round(ride.getTotalAmountOwed() * 100.0) / 100.0));

        model.addAttribute("ridesOwedToUser", ridesOwedToUser);

        return "payment_pages/rides_owed_to_user";
    }

    @PostMapping("/pay-all-owed")
    public String pay(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }

        transactionService.payAllOwedByUser(userId);

        redirectAttributes.addFlashAttribute("successMessage", "Payment for the ride has been completed.");

        return "redirect:/payments/index";
    }

    @PostMapping("/{rideId}/pay")
    public String payForRide(@PathVariable Long rideId, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }

        transactionService.payForRide(userId, rideId);

        redirectAttributes.addFlashAttribute("successMessage", "Payment for the ride has been completed.");

        return "redirect:/payments/index";
    }

}
