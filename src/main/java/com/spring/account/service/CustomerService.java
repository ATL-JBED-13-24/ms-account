package com.spring.account.service;

import com.spring.account.dto.request.CustomerRequest;
import com.spring.account.dto.response.CustomerResponse;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerService {
    CustomerResponse findCostumerById(Long id);
    CustomerResponse createCustomer(CustomerRequest request);
    void deleteCustomerById(Long id);
    void decreaseCustomerBalanceById(Long id, BigDecimal amount);
    void increaseCustomerBalanceById(Long id, BigDecimal amount);
    void updateCustomer(Long id, CustomerRequest customerRequest);

    List<CustomerResponse> getAllCostumers();
}
