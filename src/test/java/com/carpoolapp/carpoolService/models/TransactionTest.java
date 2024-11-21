package com.carpoolapp.carpoolService.models;

import com.carpoolapp.carpoolService.models.enums.TransactionStatus;
import com.carpoolapp.carpoolService.models.enums.TransactionType;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;

public class TransactionTest {

    @Test
    public void testTransactionEntity() {
        // Create User and Fare instances
        User user = new User();

        Fare fare = new Fare();
        fare.setAmount(20.0);


        // Create Transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(20.0);
        transaction.setUser(user);
        transaction.setFare(fare);
        transaction.setType(TransactionType.CREDIT);  // or TransactionType.DEBIT based on use case
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setDescription("Payment for ride");
        transaction.setCompletedDate(LocalDate.of(2024, 11, 21));

        // Assertions
        assertThat(transaction).isNotNull();
        assertThat(transaction.getAmount()).isEqualTo(20.0);
        assertThat(transaction.getUser()).isEqualTo(user);
        assertThat(transaction.getFare()).isEqualTo(fare);
        assertThat(transaction.getType()).isEqualTo(TransactionType.CREDIT);
        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(transaction.getDescription()).isEqualTo("Payment for ride");
        assertThat(transaction.getCompletedDate()).isEqualTo(LocalDate.of(2024, 11, 21));
    }

    @Test
    public void testTransactionStatusEnum() {
        // Test if the TransactionStatus enum works correctly
        assertThat(TransactionStatus.PENDING).isEqualTo(TransactionStatus.valueOf("PENDING"));
        assertThat(TransactionStatus.COMPLETED).isEqualTo(TransactionStatus.valueOf("COMPLETED"));
//        assertThat(TransactionStatus.FAILED).isEqualTo(TransactionStatus.valueOf("FAILED"));
    }

    @Test
    public void testTransactionTypeEnum() {
        // Test if the TransactionType enum works correctly
        assertThat(TransactionType.CREDIT).isEqualTo(TransactionType.valueOf("CREDIT"));
        assertThat(TransactionType.DEBIT).isEqualTo(TransactionType.valueOf("DEBIT"));
    }
}
