package com.carpoolapp.carpoolService.controller;

import com.carpoolapp.carpoolService.dto.PaymentSummaryDto;
import com.carpoolapp.carpoolService.respository.TransactionRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/index")
    public String index(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        Double totalOwed = transactionRepository.findTotalOwedByUser(userId, LocalDate.now(), LocalTime.now());
        Double totalOwing = transactionRepository.findTotalOwedToUser(userId, LocalDate.now(), LocalTime.now());

        PaymentSummaryDto paymentSummaryDto = new PaymentSummaryDto();
        paymentSummaryDto.setTotalOwed(totalOwed != null ? (Math.round(totalOwed * 100.0) / 100.0) : 0.0);
        paymentSummaryDto.setTotalOwing(totalOwing != null ? (Math.round(totalOwing * 100.0) / 100.0) : 0.0);

        model.addAttribute("paymentSummary", paymentSummaryDto);

        return "payment_pages/index";
    }
}
