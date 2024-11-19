package com.spring.account.service.impl;

import com.spring.account.dto.response.CustomerResponse;
import com.spring.account.mapper.CustomerMapper;
import com.spring.account.model.Customer;
import com.spring.account.repository.CustomerRepository;
import org.junit.jupiter.api.*;
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


//@SpringBootTest(classes = CustomerServiceImpl.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles(profiles = "integration")
//@EnableConfigurationProperties
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
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

        //Act

        CustomerResponse response = customerService.findCostumerById(customerId);


        assertNotNull(response);
        assertEquals(customerId, response.getId());

        verify(customerRepository).findById(customerId);
        verify(customerMapper).entityToResponse(mockCustomer);

    }


    @BeforeAll
    static void beforeAll() {

    }

    @BeforeEach
    void setUp() {

    }


    @AfterEach
    void tearDown() {

    }

    @AfterAll
    static void afterAll() {

    }


    @Test
    void findCostumerById() {


    }
}