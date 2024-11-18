package com.spring.account.controller;

import com.spring.account.dto.request.CustomerRequest;
import com.spring.account.dto.response.CustomerResponse;
import com.spring.account.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    @PutMapping("/{id}/decrease-balance")
    public ResponseEntity<String> decreaseCostumerBalanceById(@PathVariable Long id, @RequestParam BigDecimal amount) {
        customerService.decreaseCustomerBalanceById(id, amount);
        return ResponseEntity.status(HttpStatus.OK).body("Balance decreased successfully");
    }

    @PutMapping("/{id}/increase-balance")
    public ResponseEntity<String> increaseCustomerBalanceById(@PathVariable Long id, @RequestParam BigDecimal amount) {
        customerService.increaseCustomerBalanceById(id, amount);
        return ResponseEntity.status(HttpStatus.OK).body("Balance increased successfully");
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<String> updateCustomer(@PathVariable Long id, @RequestBody CustomerRequest request) {
        customerService.updateCustomer(id, request);
        return ResponseEntity.status(HttpStatus.OK).body("Customer updated successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findCustomerById(@PathVariable Long id) {
        CustomerResponse customerResponse = customerService.findCostumerById(id);
        return ResponseEntity.ok(customerResponse);
    }

    @PostMapping
    public ResponseEntity<String> createCustomer(@RequestBody CustomerRequest request) {
        customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Customer created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable Long id) {
        customerService.deleteCustomerById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Customer deleted successfully");
    }

    @GetMapping()
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getAllCostumers());
    }


}
