package com.carpoolapp.carpoolService.dto;

import lombok.Data;

@Data
public class WalletDto {
    private Long userId;
    private Double amount;
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String billingAddress;
}
