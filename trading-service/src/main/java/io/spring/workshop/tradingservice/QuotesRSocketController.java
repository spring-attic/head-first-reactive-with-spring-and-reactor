package io.spring.workshop.tradingservice;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@Controller
public class QuotesRSocketController {

	private final TradingCompanyClient tradingCompanyClient;

	private final QuotesRSocketClient quotesRSocketClient;

	public QuotesRSocketController(TradingCompanyClient tradingCompanyClient, QuotesRSocketClient quotesRSocketClient) {
		this.tradingCompanyClient = tradingCompanyClient;
		this.quotesRSocketClient = quotesRSocketClient;
	}

	@MessageMapping("quotes.feed")
	public Flux<Quote> quotesFeed(RSocketRequester client) {
		return this.quotesRSocketClient.quotesFeed();
	}

	@MessageMapping("quotes.summary.{ticker}")
	public Mono<TradingCompanySummary> quotesDetails(@DestinationVariable String ticker) {
		return tradingCompanyClient.getTradingCompany(ticker)
				.zipWith(this.quotesRSocketClient.getLatestQuote(ticker),
						TradingCompanySummary::new);
	}

	@MessageExceptionHandler(TickerNotFoundException.class)
	public Mono<Quote> onTickerNotFound(TickerNotFoundException e) {
		return Mono.empty();
	}

}