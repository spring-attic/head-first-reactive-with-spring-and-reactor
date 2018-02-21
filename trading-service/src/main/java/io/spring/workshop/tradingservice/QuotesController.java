package io.spring.workshop.tradingservice;

import reactor.core.publisher.Flux;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@Controller
public class QuotesController {

	private final QuotesClient quotesClient;

	public QuotesController(QuotesClient quotesClient) {
		this.quotesClient = quotesClient;
	}

	@GetMapping(path = "/quotes/feed", produces = TEXT_EVENT_STREAM_VALUE)
	@ResponseBody
	public Flux<Quote> quotesFeed() {
		return this.quotesClient.quotesFeed();
	}

}