package io.spring.workshop.tradingservice;

public class TradingCompanySummary {

	private final Quote latestQuote;

	private final TradingCompany tradingCompany;

	public TradingCompanySummary(TradingCompany tradingCompany, Quote latestQuote) {
		this.latestQuote = latestQuote;
		this.tradingCompany = tradingCompany;
	}

	public Quote getLatestQuote() {
		return latestQuote;
	}

	public TradingCompany getTradingCompany() {
		return tradingCompany;
	}
}