package com.carpoolapp.carpoolService.controller;

import com.carpoolapp.carpoolService.dto.WalletDto;
import com.carpoolapp.carpoolService.models.Wallet;
import com.carpoolapp.carpoolService.service.WalletService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/")
    public String viewWallet(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/auth/login";
        }

        Wallet wallet = walletService.getWalletByUserId(userId);

        if (wallet == null) {
            model.addAttribute("walletExists", false);
            model.addAttribute("message", "You haven't set up your wallet yet. Enter your details below to load money.");
        } else {
            model.addAttribute("walletExists", true);
            model.addAttribute("wallet", wallet);
        }

        return "wallet_pages/index";
    }

    @PostMapping("/create")
    public String createWallet(WalletDto walletDto, HttpSession session, RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/auth/login";
        }

        try {
            walletDto.setUserId(userId);
            walletService.createWallet(walletDto);
            redirectAttributes.addFlashAttribute("message", "Wallet created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create wallet. Please try again.");
        }

        return "redirect:/wallet/";
    }

    @GetMapping("/update")
    public String showUpdateWalletPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/auth/login";
        }

        Wallet wallet = walletService.getWalletByUserId(userId);

        if (wallet == null) {
            model.addAttribute("error", "No wallet found. Please set up your wallet first.");
            return "redirect:/wallet/";
        }

        model.addAttribute("wallet", wallet);

        return "wallet_pages/update";
    }

    @PostMapping("/update")
    public String updateWallet(WalletDto walletDto, HttpSession session, RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/auth/login";
        }

        walletDto.setUserId(userId);
        walletService.updateWallet(walletDto);
        redirectAttributes.addFlashAttribute("message", "Wallet updated successfully!");

        return "redirect:/wallet/";
    }

    @PostMapping("/load")
    public String loadMoney(WalletDto walletDto, HttpSession session, RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/auth/login";
        }

        walletDto.setUserId(userId);
        walletService.loadMoney(walletDto);
        redirectAttributes.addFlashAttribute("message", "Amount loaded successfully!");

        return "redirect:/wallet/";
    }
}
