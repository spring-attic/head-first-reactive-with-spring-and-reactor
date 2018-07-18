package io.spring.workshop.stockquotes;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Component;

@Component
public class QuoteGenerator {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private final MathContext mathContext = new MathContext(2);

	private final Random random = new Random();

	private final List<Quote> prices = new ArrayList<>();

	private final Flux<Quote> quoteStream;

	/**
	 * Bootstraps the generator with tickers and initial prices
	 */
	public QuoteGenerator() {
		initializeQuotes();
		this.quoteStream = getQuoteStream();
	}

	/**
	 * Fetch a Flux of streaming stock quotes.
	 * NB: Calling this method will stream quotes with randomly change price until the application is terminated.
	 * @return - a continuous stream of stock quotes
	 */
	public Flux<Quote> fetchQuoteStream() {
		// Returns streaming stock quotes
		return quoteStream;
	}

	/**
	 * Fetch a Flux of Quotes with random changes in Quotes prices as defined in List of "prices" on each invocation
	 * of this method.
	 * @return - a Flux of Quote with randomly changing prices.
	 */
	public Flux<Quote> fetchQuotesRandom(){
		try {
			Thread.sleep(1000L);  // add some delay just for testing
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Return a Flux of random stock quotes
		return Flux.fromIterable(generateQuotes(random.nextInt()));
	}

	private void initializeQuotes() {
		this.prices.add(new Quote("CTXS", 82.26));
		this.prices.add(new Quote("DELL", 63.74));
		this.prices.add(new Quote("GOOG", 847.24));
		this.prices.add(new Quote("MSFT", 65.11));
		this.prices.add(new Quote("ORCL", 45.71));
		this.prices.add(new Quote("RHT", 84.29));
		this.prices.add(new Quote("VMW", 92.21));
	}


	private Flux<Quote> getQuoteStream() {
		return Flux.interval(Duration.ofMillis(200))
				.onBackpressureDrop()
				.map(this::generateQuotes)
				.flatMapIterable(quotes -> quotes)
				.share();
	}

	private List<Quote> generateQuotes(long i) {
		logger.info("Generating quotes for transactionId="+i);
		Instant instant = Instant.now();
		return prices.stream()
				.map(baseQuote -> {
					BigDecimal priceChange = baseQuote.getPrice()
							.multiply(new BigDecimal(0.05 * this.random.nextDouble()), this.mathContext);

					Quote result = new Quote(baseQuote.getTicker(), baseQuote.getPrice().add(priceChange));
					result.setInstant(instant);
					return result;
				})
				.collect(Collectors.toList());
	}
}
