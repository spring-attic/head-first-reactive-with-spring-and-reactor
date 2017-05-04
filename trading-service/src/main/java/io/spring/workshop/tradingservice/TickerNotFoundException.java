package io.spring.workshop.tradingservice;

public class TickerNotFoundException extends RuntimeException {

	public TickerNotFoundException(String message) {
		super(message);
	}
}
