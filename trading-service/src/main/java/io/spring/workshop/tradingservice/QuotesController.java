package io.spring.workshop.tradingservice;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
public class QuotesController {

	private final TradingCompanyService tradingCompanyService;

	private final ScheduledExecutorService executorService;

	private final RestTemplate restTemplate;

	public QuotesController(TradingCompanyService tradingCompanyService, RestTemplateBuilder restTemplateBuilder) {
		this.tradingCompanyService = tradingCompanyService;
		this.restTemplate = restTemplateBuilder.build();
		this.executorService = Executors.newSingleThreadScheduledExecutor();
	}

	@GetMapping(path = "/quotes/feed", produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public List<?> latestQuotes() throws Exception {
		RequestEntity requestEntity = RequestEntity.get(new URI("http://localhost:8081/quotes?take=7"))
				.accept(APPLICATION_JSON)
				.build();

		return this.restTemplate
				.exchange(requestEntity, List.class)
				.getBody();
	}

	@GetMapping(path = "/quotes/summary/{ticker}", produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public TradingCompanySummary quotesDetails(@PathVariable String ticker) {
		TradingCompany tradingCompany = this.restTemplate.exchange(
				get("http://localhost:8082/details/{ticker}", ticker),
				TradingCompany.class)
				.getBody();

		if (tradingCompany == null) {
			throw new TickerNotFoundException(String.format("Unknown ticker: %s", ticker));
		}


		Quote quote = null;
		Instant timeout = Instant.now().plus(Duration.ofSeconds(15));

		while (quote == null && Instant.now().isBefore(timeout)) {
			List<Quote> candidates = this.restTemplate.exchange(
					get("http://localhost:8081/quotes?take=7"),
					new ParameterizedTypeReference<List<Quote>>() {
					})
					.getBody();

			quote = candidates.stream()
					.filter(q -> q.getTicker().equalsIgnoreCase(ticker))
					.findAny()
					.orElse(null);
		}

		if (quote == null) {
			quote = new Quote(ticker);
		}

		return new TradingCompanySummary(quote, tradingCompany);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(TickerNotFoundException.class)
	public void onTickerNotFound() {
	}

	private RequestEntity<?> get(String uri, Object... args) {
		return RequestEntity
				.get(new UriTemplate(uri).expand(args))
				.accept(APPLICATION_JSON)
				.build();
	}

}