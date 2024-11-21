package com.carpoolapp.carpoolService.models;

import com.carpoolapp.carpoolService.models.enums.TransactionStatus;
import com.carpoolapp.carpoolService.models.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    @ManyToOne
    @JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "fk_transaction_user"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "fareId", foreignKey = @ForeignKey(name = "fk_transaction_fare"))
    private Fare fare;

    private TransactionType type;
    private TransactionStatus status;
    private String description;
    private LocalDate completedDate;
}
