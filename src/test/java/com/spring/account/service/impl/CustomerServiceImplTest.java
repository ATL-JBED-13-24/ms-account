package com.spring.account.service.impl;

import com.spring.account.dto.request.CustomerRequest;
import com.spring.account.dto.response.CustomerResponse;
import com.spring.account.exception.InsufficientAmountException;
import com.spring.account.exception.ResourceNotFoundException;
import com.spring.account.mapper.CustomerMapper;
import com.spring.account.model.Customer;
import com.spring.account.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    CustomerMapper customerMapper;

    @Test
    @DisplayName("Find Customer By ID - Returns CustomerResponse for Existing ID")
    public void givenExistingCustomerId_WhenFindCustomerById_ThenCustomerResponseReturned() {
        Long customerId = 1L;
        Customer mockCustomer = Customer.builder().id(customerId)
                .firstName("Murad")
                .lastName("Sharif")
                .registeredAt(LocalDateTime.now())
                .build();
        CustomerResponse mockResponse = new CustomerResponse();
        mockResponse.setId(customerId);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));
        when(customerMapper.entityToResponse(mockCustomer)).thenReturn(mockResponse);

        CustomerResponse response = customerService.findCostumerById(customerId);

        assertNotNull(response);
        assertEquals(customerId, response.getId());
        verify(customerRepository).findById(customerId);
        verify(customerMapper).entityToResponse(mockCustomer);
    }

    @Test
    @DisplayName("Find Customer By ID - Throws Exception for Non-Existing ID")
    public void givenNonExistingCustomerId_WhenFindCustomerById_ThenResourceNotFoundExceptionThrown() {
        Long invalidId = 99L;
        when(customerRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.findCostumerById(invalidId));
        verify(customerRepository).findById(invalidId);
    }

    @Test
    @DisplayName("Create Customer - Returns CustomerResponse for Valid Request")
    public void givenValidCustomerRequest_WhenCreateCustomer_ThenCustomerResponseReturned() {
        CustomerRequest mockRequest = new CustomerRequest();
        Customer mockCustomer = new Customer();
        Customer savedCustomer = Customer.builder().id(1L)
                .firstName("Murad")
                .lastName("Sharif")
                .registeredAt(LocalDateTime.now())
                .build();
        CustomerResponse mockResponse = new CustomerResponse();

        when(customerMapper.requestToEntity(mockRequest)).thenReturn(mockCustomer);
        when(customerRepository.save(mockCustomer)).thenReturn(savedCustomer);
        when(customerMapper.entityToResponse(savedCustomer)).thenReturn(mockResponse);

        CustomerResponse result = customerService.createCustomer(mockRequest);

        assertNotNull(result);
        verify(customerMapper).requestToEntity(mockRequest);
        verify(customerRepository).save(mockCustomer);
        verify(customerMapper).entityToResponse(savedCustomer);
    }

    @Test
    @DisplayName("Delete Customer By ID - Deletes Customer for Existing ID")
    public void givenExistingCustomerId_WhenDeleteCustomerById_ThenCustomerDeleted() {
        Long id = 1L;
        Customer mockCustomer = Customer.builder().id(id)
                .firstName("Murad")
                .lastName("Sharif")
                .registeredAt(LocalDateTime.now())
                .build();

        when(customerRepository.findById(id)).thenReturn(Optional.of(mockCustomer));

        customerService.deleteCustomerById(id);

        verify(customerRepository).findById(id);
        verify(customerRepository).delete(mockCustomer);
    }

    @Test
    @DisplayName("Decrease Customer Balance - Decreases Balance for Sufficient Funds")
    void givenSufficientBalanceAndValidId_WhenDecreaseCustomerBalanceById_ThenBalanceDecreased() {
        Long customerId = 1L;
        BigDecimal amount = new BigDecimal("50.00");
        Customer mockCustomer = new Customer();
        mockCustomer.setBalance(new BigDecimal("100.00"));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));

        customerService.decreaseCustomerBalanceById(customerId, amount);

        assertEquals(new BigDecimal("50.00"), mockCustomer.getBalance());
        verify(customerRepository).findById(customerId);
        verify(customerRepository).save(mockCustomer);
    }

    @Test
    @DisplayName("Decrease Customer Balance - Throws Exception for Insufficient Funds")
    void givenInsufficientBalance_WhenDecreaseCustomerBalanceById_ThenInsufficientAmountExceptionThrown() {
        Long customerId = 1L;
        BigDecimal amount = new BigDecimal("150.00");
        Customer mockCustomer = new Customer();
        mockCustomer.setBalance(new BigDecimal("100.00"));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));

        assertThrows(InsufficientAmountException.class, () -> customerService.decreaseCustomerBalanceById(customerId, amount));
        verify(customerRepository).findById(customerId);
    }

    @Test
    @DisplayName("Increase Customer Balance - Increases Balance for Valid Amount")
    void givenValidAmountAndCustomerId_WhenIncreaseCustomerBalanceById_ThenBalanceIncreased() {
        Long customerId = 1L;
        BigDecimal amount = new BigDecimal("50.00");
        Customer mockCustomer = new Customer();
        mockCustomer.setBalance(new BigDecimal("100.00"));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));

        customerService.increaseCustomerBalanceById(customerId, amount);

        assertEquals(new BigDecimal("150.00"), mockCustomer.getBalance());
        verify(customerRepository).findById(customerId);
        verify(customerRepository).save(mockCustomer);
    }
}
