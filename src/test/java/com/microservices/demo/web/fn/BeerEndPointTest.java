package com.microservices.demo.web.fn;

import com.microservices.demo.model.BeerDTO;
import com.microservices.demo.service.BeerServiceImplTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;


import static net.bytebuddy.implementation.FixedValue.value;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class BeerEndPointTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void listOfBeersTest() {
        webTestClient.get().uri(BeerRouterConfig.BEER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("$.size()", value(greaterThan(1)));
    }

    @Test
    void testGetById() {
        BeerDTO beerDTO = getSavedTestBeer();
        webTestClient.get().uri(BeerRouterConfig.BEER_PATH_ID, beerDTO.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody(BeerDTO.class);
    }

    @Test
    void testGetByIdNotFound() {
        webTestClient.get().uri(BeerRouterConfig.BEER_PATH_ID, "1")
                .exchange()
                .expectStatus().isNotFound();
    }

    public BeerDTO getSavedTestBeer() {
        FluxExchangeResult<BeerDTO> fluxExchangeResult = webTestClient.post().uri(BeerRouterConfig.BEER_PATH)
                .body(Mono.just(BeerServiceImplTest.getTestBeer()), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .returnResult(BeerDTO.class);

        return webTestClient.get().uri(BeerRouterConfig.BEER_PATH)
                .exchange().returnResult(BeerDTO.class).getResponseBody().blockFirst();
    }

    @Test
    void testCreateBeer() {
        BeerDTO testDTO = getSavedTestBeer();

        webTestClient.post().uri(BeerRouterConfig.BEER_PATH)
                .body(Mono.just(testDTO), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("location");
    }

    @Test
    void testUpdateBeerById() {
        BeerDTO testDTO = getSavedTestBeer();
        testDTO.setBeerName("TEST_ABC");

        webTestClient.put().uri(BeerRouterConfig.BEER_PATH_ID, testDTO.getId())
                .body(Mono.just(testDTO), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Order(999)
    @Test
    void testDeleteBeerById() {

        BeerDTO beerDTO = getSavedTestBeer();

        webTestClient.delete().uri(BeerRouterConfig.BEER_PATH_ID, beerDTO.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testUpdateBeerByIdNotFound() {
        BeerDTO testDTO = getSavedTestBeer();
        testDTO.setId("1");
        testDTO.setBeerName("TEST_ABC");

        webTestClient.put().uri(BeerRouterConfig.BEER_PATH_ID, testDTO.getId())
                .body(Mono.just(testDTO), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteBeerByIdNotFound() {

        BeerDTO beerDTO = getSavedTestBeer();
        beerDTO.setId("1");

        webTestClient.delete().uri(BeerRouterConfig.BEER_PATH_ID, beerDTO.getId())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateBeerByIdBadRequest() {
        BeerDTO beerDTO = getSavedTestBeer();
        beerDTO.setBeerName("A");

        webTestClient.put().uri(BeerRouterConfig.BEER_PATH_ID, beerDTO.getId())
                .body(Mono.just(beerDTO), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }
}
