package io.spring.workshop.tradingservice;

import java.time.Duration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@Controller
public class QuotesController {

	private final TradingCompanyService tradingCompanyService;

	public QuotesController(TradingCompanyService tradingCompanyService) {
		this.tradingCompanyService = tradingCompanyService;
	}

	@GetMapping(path = "/quotes/feed", produces = TEXT_EVENT_STREAM_VALUE)
	@ResponseBody
	public Flux<Quote> quotesStream() {
		return WebClient.create("http://localhost:8081")
				.get()
				.uri("/quotes")
				.accept(APPLICATION_STREAM_JSON)
				.retrieve()
				.bodyToFlux(Quote.class);
	}

	@GetMapping(path = "/quotes/details/{ticker}", produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public Mono<TradingCompanyLatestQuote> quotesDetails(@PathVariable String ticker) {
		return WebClient.create("http://localhost:8081")
				.get()
				.uri("/quotes")
				.accept(APPLICATION_STREAM_JSON)
				.retrieve()
				.bodyToFlux(Quote.class)
				.filter(q -> q.getTicker().equalsIgnoreCase(ticker))
				.next()
				.timeout(Duration.ofSeconds(15), Mono.just(new Quote(ticker)))
				.zipWith(tradingCompanyService.getTradingCompany(ticker)
								.switchIfEmpty(Mono.error(new TickerNotFoundException("Unknown Ticker: "+ticker))),
						TradingCompanyLatestQuote::new);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(TickerNotFoundException.class)
	public void onTickerNotFound() {
	}

}