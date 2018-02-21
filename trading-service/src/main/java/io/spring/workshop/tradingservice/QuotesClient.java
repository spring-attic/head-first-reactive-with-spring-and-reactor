package io.spring.workshop.tradingservice;

import reactor.core.publisher.Flux;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;

@Component
public class QuotesClient {

	private final WebClient webClient;

	public QuotesClient(WebClient.Builder webclientBuilder) {
		this.webClient = webclientBuilder.build();
	}

	public Flux<Quote> quotesFeed() {
		return this.webClient.get().uri("http://localhost:8081/quotes")
				.accept(APPLICATION_STREAM_JSON)
				.retrieve()
				.bodyToFlux(Quote.class);
	}
}
