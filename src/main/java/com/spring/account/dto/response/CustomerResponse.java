package com.spring.account.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CustomerResponse {

    Long id;

    String firstName;

    String lastName;

    LocalDateTime registeredAt;

    BigDecimal balance;

    String address;

    String phoneNumber;
}

