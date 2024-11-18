package com.spring.account.repository;

import com.spring.account.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Modifying
    @Query("UPDATE Customer c SET c.balance = c.balance - :amount WHERE c.id = :id")
    void decreaseCustomerBalance(@Param("id") Long id, @Param("amount") BigDecimal amount);

    @Modifying
    @Query("UPDATE Customer c SET c.balance = c.balance + :amount WHERE c.id = :id")
    void increaseCustomerBalanceById(@Param("id") Long id, @Param("amount") BigDecimal amount);


}
