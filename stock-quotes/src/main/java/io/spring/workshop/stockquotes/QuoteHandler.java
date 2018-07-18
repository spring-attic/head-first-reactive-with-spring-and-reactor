package io.spring.workshop.stockquotes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.SignalType;

import java.util.function.Consumer;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class QuoteHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private final QuoteGenerator quoteGenerator;

	public QuoteHandler(QuoteGenerator quoteGenerator) {
		this.quoteGenerator = quoteGenerator;
	}

	/**
	 * This method will stream quotes with randomly change prices until the client terminate the request or the
	 * stockquotes service/application is terminated.
	 * @param request - the reactive ServerRequest object
	 * @return - a Mono of ServerResponse streaming JSON
	 */
	public Mono<ServerResponse> streamQuotes(ServerRequest request) {
		return ok()
				.contentType(APPLICATION_STREAM_JSON)
				.body(this.quoteGenerator.fetchQuoteStream(), Quote.class);
	}

	/**
	 * Non-streaming version of this.streamQuotes method.
	 * This method Adds behavior to be triggered after the transaction terminates for any reason, including cancellation.
	 * @param request - the reactive ServerRequest object
	 * @return - a Mono of ServerResponse (non-streaming) JSON per request.
	 */
	public Mono<ServerResponse> fetchQuotes(ServerRequest request) {
		long startTimeMillis = System.currentTimeMillis();
		return ok()
				.contentType(APPLICATION_JSON)
				.body(this.quoteGenerator.fetchQuotesRandom(), Quote.class)
				.doFinally(onTransactionComplete(startTimeMillis));
	}

	/**
	 * @param startTimeMillis - the startTime (in Millis) of the transaction
	 * @return  - a Consumer with the behavior to be triggered after the Mono terminates.
	 */
	private Consumer<SignalType> onTransactionComplete(long startTimeMillis) {
		return signalType -> {
			logger.info(String.format("SignalType=%s",signalType.name()));
            onComplete(System.currentTimeMillis() - startTimeMillis);
        };
	}

	private void onComplete(long elapseTimeMillis) {
		logger.info("transactionElapseTimeMillis="+elapseTimeMillis);
	}
}
