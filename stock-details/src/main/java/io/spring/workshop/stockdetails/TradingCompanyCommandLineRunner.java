/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package io.spring.workshop.stockdetails;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TradingCompanyCommandLineRunner implements CommandLineRunner {

	private final TradingCompanyRepository repository;

	public TradingCompanyCommandLineRunner(TradingCompanyRepository repository) {
		this.repository = repository;
	}

	@Override
	public void run(String... strings) {
		List<TradingCompany> companies = Arrays.asList(
				new TradingCompany("Citrix Systems", "CTXS"),
				new TradingCompany("Dell Technologies", "DELL"),
				new TradingCompany("Google", "GOOG"),
				new TradingCompany("Microsoft", "MSFT"),
				new TradingCompany("Oracle", "ORCL"),
				new TradingCompany("Red Hat", "RHT"),
				new TradingCompany("Vmware", "VMW")
		);
		this.repository.insert(companies).blockLast(Duration.ofSeconds(30));
	}

}
