package com.microservices.demo.repositories;

import com.microservices.demo.domain.Beer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BeerRepository extends ReactiveMongoRepository<Beer, String> {
}
