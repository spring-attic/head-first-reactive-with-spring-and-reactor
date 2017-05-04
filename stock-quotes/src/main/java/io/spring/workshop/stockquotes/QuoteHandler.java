package io.spring.workshop.stockquotes;

import reactor.core.publisher.Mono;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class QuoteHandler {

	private final QuoteGenerator quoteGenerator;

	public QuoteHandler(QuoteGenerator quoteGenerator) {
		this.quoteGenerator = quoteGenerator;
	}

	public Mono<ServerResponse> fetchQuotes(ServerRequest request) {
		return ok()
				.contentType(APPLICATION_JSON)
				.body(this.quoteGenerator.fetchQuoteStream()
						.take(request.queryParam("take")
								.map(Integer::parseInt)
								.orElse(7)), Quote.class);
	}
}