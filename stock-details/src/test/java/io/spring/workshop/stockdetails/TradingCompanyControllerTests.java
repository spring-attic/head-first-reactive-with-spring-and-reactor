/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package io.spring.workshop.stockdetails;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@WebFluxTest(TradingCompanyController.class)
public class TradingCompanyControllerTests {

  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  private TradingCompanyRepository repository;

  @Test
  public void listTradingCompanies() {
    TradingCompany soo = new TradingCompany("1", "The Sooshi Company", "SOO");
    TradingCompany pizza = new TradingCompany("2", "Pizza & friends", "PIZZA");

    BDDMockito.given(this.repository.findAll())
        .willReturn(Flux.just(soo, pizza));

    this.webTestClient.get().uri("/details").accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectBodyList(TradingCompany.class)
        .hasSize(2)
        .contains(soo, pizza);

  }

  @Test
  public void getTradingCompany() {
    TradingCompany soo = new TradingCompany("1", "The Sooshi Company", "SOO");

    BDDMockito.given(this.repository.findByTicker("SOO"))
        .willReturn(Mono.just(soo));

    this.webTestClient.get().uri("/details/SOO").accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectBody(TradingCompany.class)
        .isEqualTo(soo);
  }

}

