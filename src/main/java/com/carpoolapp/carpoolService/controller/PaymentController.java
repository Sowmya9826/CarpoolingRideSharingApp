package com.carpoolapp.carpoolService.controller;

import com.carpoolapp.carpoolService.dto.PaymentSummaryDto;
import com.carpoolapp.carpoolService.dto.RideOwedDto;
import com.carpoolapp.carpoolService.dto.RideOwedToUserDto;
import com.carpoolapp.carpoolService.models.User;
import com.carpoolapp.carpoolService.models.Wallet;
import com.carpoolapp.carpoolService.repository.TransactionRepository;
import com.carpoolapp.carpoolService.service.RideService;
import com.carpoolapp.carpoolService.service.TransactionService;
import com.carpoolapp.carpoolService.service.WalletService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    private WalletService walletService;

    @Autowired
    private RideService rideService;

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
    public String pay(@RequestParam("amount") Double amount, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }

        if (amount > 0) {
            Wallet wallet = walletService.getWalletByUserId(userId);
            if (wallet == null || wallet.getBalance() < amount) {
                redirectAttributes.addFlashAttribute("errorMessage", "Not enough money in wallet! Please load your wallet to pay");

                return "redirect:/wallet/";
            }

            walletService.pay(wallet, amount);
        }

        transactionService.payAllOwedByUser(userId);

        redirectAttributes.addFlashAttribute("successMessage", "Payment for the ride has been completed.");

        return "redirect:/payments/index";
    }

    @PostMapping("/{rideId}/pay")
    public String payForRide(@PathVariable Long rideId, @RequestParam("amount") Double amount, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }

        if (amount > 0) {
            Wallet wallet = walletService.getWalletByUserId(userId);
            if (wallet == null || wallet.getBalance() < amount) {
                redirectAttributes.addFlashAttribute("errorMessage", "Not enough money in wallet! Please load your wallet to pay");

                return "redirect:/wallet/";
            }

            walletService.pay(wallet, amount);

            // Fetch the driver for this ride and credit their wallet
            User driver = rideService.getDriver(rideId);
            Wallet driverWallet = walletService.getWalletByUserId(driver.getId());
            if (driverWallet != null) {
                walletService.credit(driverWallet, amount);
            }
        }

        transactionService.payForRide(userId, rideId);

        redirectAttributes.addFlashAttribute("successMessage", "Payment for the ride has been completed.");

        return "redirect:/payments/index";
    }

}
