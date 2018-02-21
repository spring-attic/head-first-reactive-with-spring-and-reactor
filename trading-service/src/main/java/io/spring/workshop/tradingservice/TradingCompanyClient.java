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
public class TradingCompanyClient {

	private final WebClient webClient;

	public TradingCompanyClient(WebClient.Builder builder) {
		this.webClient = builder.build();
	}

	public Flux<TradingCompany> findAllCompanies() {
		return this.webClient.get()
				.uri("http://localhost:8082/details")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToFlux(TradingCompany.class);
	}

	public Mono<TradingCompany> getTradingCompany(String ticker) {
		return this.webClient.get()
				.uri("http://localhost:8082/details/{ticker}", ticker)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(TradingCompany.class)
				.switchIfEmpty(Mono.error(new TickerNotFoundException("Unknown Ticker: "+ticker)));
	}
}
