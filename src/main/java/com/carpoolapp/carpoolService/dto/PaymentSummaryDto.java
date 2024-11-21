package com.carpoolapp.carpoolService.dto;

import lombok.Data;

@Data
public class PaymentSummaryDto {
    double totalOwed;
    double totalOwing;
}
