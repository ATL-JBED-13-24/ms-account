package com.spring.account.service.impl;

import com.spring.account.dto.request.CustomerRequest;
import com.spring.account.dto.response.CustomerResponse;
import com.spring.account.exception.InsufficientAmountException;
import com.spring.account.exception.ResourceNotFoundException;
import com.spring.account.mapper.CustomerMapper;
import com.spring.account.model.Customer;
import com.spring.account.repository.CustomerRepository;
import com.spring.account.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerResponse findCostumerById(Long id) {
        log.info("Trying to find customer with ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with the given ID: " + id));
        return customerMapper.entityToResponse(customer);
    }

    @Transactional
    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        log.info("Attempting to create customer with details: {}", request);

        Customer customer = customerMapper.requestToEntity(request);

        customer.setBalance(BigDecimal.ZERO);

        Customer savedCustomer = customerRepository.save(customer);

        log.info("Customer created successfully with ID: {}", savedCustomer.getId());
        return customerMapper.entityToResponse(savedCustomer);
    }

    @Override
    public void deleteCustomerById(Long id) {
        log.info("Attempting to delete customer with ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with the given ID: " + id));
        customerRepository.delete(customer);
        log.info("Customer with ID: {} has been successfully deleted", id);
    }

    @Override
    public void decreaseCustomerBalanceById(Long id, BigDecimal amount) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with the given ID: " + id));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Insufficient amount");
            throw new InsufficientAmountException("Failed transaction: amount is negative or zero");
        }

        log.info("Trying to decrease customer balance. Customer ID: {}, Amount: {}", id, amount);
        if (customer.getBalance().compareTo(amount) < 0) {
            throw new InsufficientAmountException(
                    "Insufficient balance for the transaction. Current balance: " + customer.getBalance());
        }

        customer.setBalance(customer.getBalance().subtract(amount));
        customerRepository.save(customer);
        log.info("Customer balance decreased. Customer ID: {}, New Balance: {}", id, customer.getBalance());
    }

    @Override
    public void increaseCustomerBalanceById(Long id, BigDecimal amount) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with the given ID: " + id));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Failed transaction, amount: {}", amount);
            throw new InsufficientAmountException("Failed transaction: amount is negative or zero");
        }

        log.info("Trying to increase customer balance. Customer ID: {}, Amount: {}", id, amount);
        customer.setBalance(customer.getBalance().add(amount));
        customerRepository.save(customer);
        log.info("Customer balance increased. Customer ID: {}, New Balance: {}", id, customer.getBalance());
    }

    @Override
    public void updateCustomer(Long id, CustomerRequest customerRequest) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with the given ID: " + id));

        customer.setFirstName(customerRequest.getFirstName());
        customer.setLastName(customerRequest.getLastName());
        customer.setAddress(customerRequest.getAddress());
        customer.setPhoneNumber(customerRequest.getPhoneNumber());
        customer.setUpdatedAt(LocalDateTime.now());

        customerRepository.save(customer);
        log.info("Customer with ID: {} updated successfully", id);
    }

    @Override
    public List<CustomerResponse> getAllCostumers() {
        log.info("Trying to retrieve all customers");

        List<Customer> customers = customerRepository.findAll();

        if (customers.isEmpty()) {
            throw new ResourceNotFoundException("No customers exist");
        }

        return customers.stream()
                .map(customerMapper::entityToResponse)
                .toList();
    }

}
