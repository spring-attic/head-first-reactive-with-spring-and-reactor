package io.spring.workshop.stockdetails;

import reactor.core.publisher.Mono;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TradingCompanyRepository extends ReactiveMongoRepository<TradingCompany, String> {

	Mono<TradingCompany> findByTicker(String ticker);

}
