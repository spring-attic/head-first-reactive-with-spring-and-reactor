/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package io.spring.workshop.stockdetails;

import java.time.Duration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TradingCompanyController {

	private final TradingCompanyRepository tradingCompanyRepository;

	public TradingCompanyController(TradingCompanyRepository tradingCompanyRepository) {
		this.tradingCompanyRepository = tradingCompanyRepository;
	}

	@GetMapping(path = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<TradingCompany> listTradingCompanies() {
		return this.tradingCompanyRepository.findAll();
	}

	@GetMapping(path = "/details/{ticker}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<TradingCompany> showTradingCompanies(@PathVariable String ticker) {
		return this.tradingCompanyRepository.findByTicker(ticker).delayElement(Duration.ofMillis(400));
	}

}