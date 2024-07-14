package com.microservices.demo.service;

import com.microservices.demo.model.BeerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerService {

    Flux<BeerDTO> listBeers();
    Mono<BeerDTO> saveBeer(Mono<BeerDTO> dto);
    Mono<BeerDTO> saveBeer(BeerDTO dto);
    Mono<BeerDTO> getById(String id);
    Mono<BeerDTO> updateBeer(String id, BeerDTO dto);
    default Mono<BeerDTO> patchBeer(String id, BeerDTO dto) {
        return null;
    }
    Mono<Void> deleteBeerById(String id);


}
