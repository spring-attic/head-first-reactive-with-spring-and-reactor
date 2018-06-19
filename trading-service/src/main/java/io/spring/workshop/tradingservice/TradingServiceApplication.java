package io.spring.workshop.tradingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.actuate.env.EnvironmentEndpoint;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@SpringBootApplication
public class TradingServiceApplication {

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http
				.authorizeExchange()
				.pathMatchers("/details").authenticated()
				.matchers(EndpointRequest.to(EnvironmentEndpoint.class)).permitAll()
				.anyExchange().permitAll()
				.and()
				.httpBasic().and()
				.formLogin();
		return http.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(TradingServiceApplication.class, args);
	}
}
