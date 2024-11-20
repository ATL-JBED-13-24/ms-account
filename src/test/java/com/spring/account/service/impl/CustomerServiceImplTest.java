package com.spring.account.service.impl;

import com.spring.account.dto.request.CustomerRequest;
import com.spring.account.dto.response.CustomerResponse;
import com.spring.account.exception.ResourceNotFoundException;
import com.spring.account.mapper.CustomerMapper;
import com.spring.account.model.Customer;
import com.spring.account.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    public void givenNonExistingCustomerId_WhenFindCustomerById_ThenResourceNotFoundExceptionThrown() {

        Long invalidId = 99L;
        when(customerRepository.findById(invalidId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> customerService.findCostumerById(invalidId));
        verify(customerRepository).findById(invalidId);


    }


    @Test
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


}