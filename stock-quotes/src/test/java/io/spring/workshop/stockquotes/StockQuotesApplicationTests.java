package io.spring.workshop.stockquotes;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
//  We create a `@SpringBootTest`, starting an actual server on a `RANDOM_PORT`
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StockQuotesApplicationTests {

	// Spring Boot will create a `WebTestClient` for you,
	// already configure and ready to issue requests against "localhost:RANDOM_PORT"
	@Autowired
	private WebTestClient webTestClient;

	@Test
	public void fetchQuotes() {
		List<Quote> result =
				webTestClient
				// We then create a GET request to test an endpoint
				.get().uri("/quotes")
				.accept(MediaType.APPLICATION_STREAM_JSON)
				.exchange()
				// and use the dedicated DSL to test assertions against the response
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON)
				.returnResult(Quote.class)
				.getResponseBody()
				.take(20)
				.collectList()
				.block();

		assertThat(result).allSatisfy(quote -> assertThat(quote.getPrice()).isPositive());
	}

}