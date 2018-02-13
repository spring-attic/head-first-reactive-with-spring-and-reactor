/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package io.spring.workshop.tradingservice;

public class TradingCompanyLatestQuote {

	private final Quote latestQuote;

	private final TradingCompany tradingCompany;

	public TradingCompanyLatestQuote(TradingCompany tradingCompany, Quote latestQuote) {
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