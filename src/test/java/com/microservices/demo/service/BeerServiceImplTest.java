package com.microservices.demo.service;

import com.microservices.demo.App;
import com.microservices.demo.domain.Beer;
import com.microservices.demo.mappers.BeerMapper;
import com.microservices.demo.model.BeerDTO;
import com.microservices.demo.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = App.class)
public class BeerServiceImplTest {

    @Autowired
    BeerService beerService;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    BeerRepository beerRepository;

    BeerDTO beerDTO;
    Beer beer;

    @BeforeEach
    void setUp() {
        beerDTO = beerMapper.beerToBeerDto(getTestBeer());
        beer = beerRepository.save(beerMapper.beerDtoToBeer(beerDTO)).block();
    }

    @Test
    void testSaveBeer() {
        Mono<BeerDTO> beerDTOMono = beerService.saveBeer(Mono.just(beerDTO));
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<BeerDTO> savedBeerRef = new AtomicReference<>();
        beerDTOMono.subscribe(savedBeer -> {
            atomicBoolean.set(true);
            savedBeerRef.set(savedBeer);
        });

        await().untilTrue(atomicBoolean);
        assertThat(savedBeerRef).isNotNull();
    }

    @Test
    void testGetById() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<BeerDTO> beerDTOAtomicReference = new AtomicReference<>();

        beerService.getById(beer.getId()).subscribe(foundBeer -> {
           atomicBoolean.set(true);
           beerDTOAtomicReference.set(foundBeer);
        });

        await().untilTrue(atomicBoolean);
        assertThat(beerDTOAtomicReference.get().getId()).isEqualTo(beer.getId());
    }

    public static Beer getTestBeer() {
        return Beer.builder()
                .beerName("Space Dust")
                .beerStyle("IPA")
                .price(BigDecimal.TEN)
                .quantityOnHand(12)
                .upc("123213")
                .build();
    }
}