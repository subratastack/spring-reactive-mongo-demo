package com.microservices.demo.mappers;

import com.microservices.demo.domain.Customer;
import com.microservices.demo.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    CustomerDTO customerToCustomerDto(Customer entity);
    Customer customerDtoToCustomer(CustomerDTO dto);
}
