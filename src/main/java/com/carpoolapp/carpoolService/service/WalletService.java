package com.carpoolapp.carpoolService.service;

import com.carpoolapp.carpoolService.dto.WalletDto;
import com.carpoolapp.carpoolService.models.Wallet;
import com.carpoolapp.carpoolService.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    public Wallet getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId).orElse(null);
    }

    public void createWallet(WalletDto walletDto) {
        Wallet wallet = new Wallet();
        wallet.setBalance(walletDto.getAmount());
        wallet.setUserId(walletDto.getUserId());
        wallet.setCardNumber(walletDto.getCardNumber());
        wallet.setCardHolderName(walletDto.getCardHolderName());
        wallet.setExpiryDate(walletDto.getExpiryDate());
        wallet.setBillingAddress(walletDto.getBillingAddress());

        walletRepository.save(wallet);
    }

    public void updateWallet(WalletDto walletDto) {
        Wallet wallet = getWalletByUserId(walletDto.getUserId());
        wallet.setCardNumber(walletDto.getCardNumber());
        wallet.setCardHolderName(walletDto.getCardHolderName());
        wallet.setExpiryDate(walletDto.getExpiryDate());
        wallet.setBillingAddress(walletDto.getBillingAddress());

        walletRepository.save(wallet);
    }

    public void loadMoney(WalletDto walletDto) {
        Wallet wallet = getWalletByUserId(walletDto.getUserId());
        wallet.setBalance(wallet.getBalance() + walletDto.getAmount());

        walletRepository.save(wallet);
    }

    public void pay(Wallet wallet, Double amount) {
        double currentBalance = wallet.getBalance();
        double newBalance = currentBalance - amount;

        // Round to 2 decimal places
        newBalance = Math.round(newBalance * 100.0) / 100.0;

        wallet.setBalance(newBalance);
        walletRepository.save(wallet);
    }

    public void credit(Wallet wallet, double amount) {
        double currentBalance = wallet.getBalance();
        double newBalance = currentBalance + amount;

        // Round to 2 decimal places
        newBalance = Math.round(newBalance * 100.0) / 100.0;

        wallet.setBalance(newBalance);
        walletRepository.save(wallet);
    }
}
