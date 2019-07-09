package io.spring.workshop.tradingservice;

import java.time.Duration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;

/**
 * @author Stephane Maldini
 */
@Component
public class QuotesRSocketClient {

	final Mono<RSocketRequester> requester;

	QuotesRSocketClient(RSocketRequester.Builder builder) {
		this.requester = builder.dataMimeType(MediaType.APPLICATION_JSON)
		                        .connectTcp("localhost", 9898)
		                        .cache()
		                        .retryBackoff(5, Duration.ofSeconds(3));
	}

	public Flux<Quote> quotesFeed() {
		return requester.flatMapMany(r -> r.route("quotes.feed")
		                                   .data(Mono.empty())
		                                   .retrieveFlux(Quote.class));

	}

	public Mono<Quote> getLatestQuote(String ticker) {
		return requester.flatMap(r -> r.route("quotes.latest")
		                               .data(ticker)
		                               .retrieveMono(Quote.class))
		                .timeout(Duration.ofSeconds(15), Mono.just(new Quote(ticker)));
	}

}
