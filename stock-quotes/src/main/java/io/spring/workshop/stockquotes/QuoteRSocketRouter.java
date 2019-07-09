package io.spring.workshop.stockquotes;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * @author Stephane Maldini
 */
@Controller
public class QuoteRSocketRouter {

	final QuoteGenerator quoteGenerator;

	QuoteRSocketRouter(QuoteGenerator quoteGenerator) {
		this.quoteGenerator = quoteGenerator;
	}

	@MessageMapping("quotes.feed")
	public Flux<Quote> quotesFeed() {
		return quoteGenerator.fetchQuoteStream();
	}

	@MessageMapping("quotes.latest")
	public Mono<Quote> latestQuote(String ticker) {
		return quoteGenerator.fetchQuoteStream()
		                     .filter(q -> q.getTicker().equalsIgnoreCase(ticker))
		                     .next();
	}
}
