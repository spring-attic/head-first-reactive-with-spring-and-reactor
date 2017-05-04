package io.spring.workshop.tradingservice;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * @author Madhura Bhave
 */
@Component
public class TradingCompanyService {


	private final RestTemplate restTemplate;

	public TradingCompanyService(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	public TradingCompany getTradingCompany(String ticker) {

		RequestEntity requestEntity = RequestEntity.get(new UriTemplate("http://localhost:8082/details/{ticker}").expand(ticker))
				.accept(APPLICATION_JSON).build();

		return this.restTemplate
				.exchange(requestEntity, TradingCompany.class)
				.getBody();
	}
}
