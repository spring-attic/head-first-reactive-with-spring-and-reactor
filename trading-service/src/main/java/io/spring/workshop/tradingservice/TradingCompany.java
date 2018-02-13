/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package io.spring.workshop.tradingservice;

public class TradingCompany {

	private String id;

	private String description;

	private String ticker;

	public TradingCompany() {
	}

	public TradingCompany(String id, String description, String ticker) {
		this.id = id;
		this.description = description;
		this.ticker = ticker;
	}

	public TradingCompany(String description, String ticker) {
		this.description = description;
		this.ticker = ticker;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TradingCompany that = (TradingCompany) o;

		if (!id.equals(that.id)) return false;
		return description.equals(that.description);
	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + description.hashCode();
		return result;
	}
}

