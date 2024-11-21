package com.carpoolapp.carpoolService.controller;

import com.carpoolapp.carpoolService.dto.PaymentSummaryDto;
import com.carpoolapp.carpoolService.dto.RideOwedDto;
import com.carpoolapp.carpoolService.respository.TransactionRepository;
import com.carpoolapp.carpoolService.service.TransactionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

        Double totalOwed = transactionRepository.findTotalOwedByUser(userId, LocalDateTime.now(ZoneId.of("UTC")).toLocalDate(),
                LocalDateTime.now(ZoneId.of("UTC")).toLocalTime());
        Double totalOwing = transactionRepository.findTotalOwedToUser(userId, LocalDateTime.now(ZoneId.of("UTC")).toLocalDate(),
                LocalDateTime.now(ZoneId.of("UTC")).toLocalTime());

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

        return "payment_pages/rides-owed";
    }

}
