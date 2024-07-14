package com.microservices.demo.service;

import com.microservices.demo.mappers.BeerMapper;
import com.microservices.demo.model.BeerDTO;
import com.microservices.demo.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Flux<BeerDTO> listBeers() {
        return beerRepository.findAll()
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDTO> saveBeer(Mono<BeerDTO> dto) {
        return dto.map(beerMapper::beerDtoToBeer)
                .flatMap(beerRepository::save)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDTO> saveBeer(BeerDTO dto) {
        return beerRepository.save(beerMapper.beerDtoToBeer(dto))
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDTO> getById(String id) {
        return beerRepository.findById(id)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDTO> updateBeer(String id, BeerDTO dto) {
        return beerRepository.findById(id).map(
                foundBeer -> {
                    //update properties
                    foundBeer.setBeerName(dto.getBeerName());
                    foundBeer.setBeerStyle(dto.getBeerStyle());
                    foundBeer.setPrice(dto.getPrice());
                    foundBeer.setUpc(dto.getUpc());
                    foundBeer.setQuantityOnHand(dto.getQuantityOnHand());

                    return foundBeer;
                }).flatMap(beerRepository::save)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<Void> deleteBeerById(String id) {
        return beerRepository.deleteById(id);
    }
}
