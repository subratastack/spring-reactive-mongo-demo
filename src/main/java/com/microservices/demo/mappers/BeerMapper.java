package com.microservices.demo.mappers;

import com.microservices.demo.domain.Beer;
import com.microservices.demo.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {
    BeerDTO beerToBeerDto(Beer entity);

    Beer beerDtoToBeer(BeerDTO beerDTO);
}
