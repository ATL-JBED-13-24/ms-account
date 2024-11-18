package com.spring.account.mapper;

import com.spring.account.dto.request.CustomerRequest;
import com.spring.account.dto.response.CustomerResponse;
import com.spring.account.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "balance", ignore = true)
    Customer requestToEntity(CustomerRequest request);

    CustomerResponse entityToResponse(Customer customer);
}
