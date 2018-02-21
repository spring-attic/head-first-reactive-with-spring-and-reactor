package io.spring.workshop.tradingservice;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@Controller
public class QuotesController {

	private final TradingCompanyClient tradingCompanyClient;

	private final QuotesClient quotesClient;

	public QuotesController(TradingCompanyClient tradingCompanyClient, QuotesClient quotesClient) {
		this.tradingCompanyClient = tradingCompanyClient;
		this.quotesClient = quotesClient;
	}

	@GetMapping(path = "/quotes/feed", produces = TEXT_EVENT_STREAM_VALUE)
	@ResponseBody
	public Flux<Quote> quotesFeed() {
		return this.quotesClient.quotesFeed();
	}

	@GetMapping(path = "/quotes/summary/{ticker}", produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public Mono<TradingCompanySummary> quotesDetails(@PathVariable String ticker) {
		return tradingCompanyClient.getTradingCompany(ticker)
				.zipWith(this.quotesClient.getLatestQuote(ticker),
						TradingCompanySummary::new);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(TickerNotFoundException.class)
	public void onTickerNotFound() {
	}

}