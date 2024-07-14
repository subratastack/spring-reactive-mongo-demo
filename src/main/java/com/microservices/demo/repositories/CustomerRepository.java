package com.microservices.demo.repositories;

import com.microservices.demo.domain.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
}
