/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package io.spring.workshop.tradingservice;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Madhura Bhave
 */
@Component
public class TradingCompanyService {

	public Flux<TradingCompany> findAllCompanies() {
		return WebClient.create("http://localhost:8082")
				.get()
				.uri("/details")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToFlux(TradingCompany.class)
				.log("io.spring.workshop.tradingservice.details");
	}

	public Mono<TradingCompany> getTradingCompany(String ticker) {
		return WebClient.create("http://localhost:8082")
				.get()
				.uri("/details/{ticker}", ticker)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(TradingCompany.class)
				.log("io.spring.workshop.tradingservice.details.ticker");
	}
}
