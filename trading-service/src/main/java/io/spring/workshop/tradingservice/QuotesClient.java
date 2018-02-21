package io.spring.workshop.tradingservice;

import java.time.Duration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

	public Mono<Quote> getLatestQuote(String ticker) {
		return quotesFeed()
				.filter(q -> q.getTicker().equalsIgnoreCase(ticker))
				.next()
				.timeout(Duration.ofSeconds(15), Mono.just(new Quote(ticker)));
	}
}
