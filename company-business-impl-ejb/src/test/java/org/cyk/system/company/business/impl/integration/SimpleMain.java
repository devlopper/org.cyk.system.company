package org.cyk.system.company.business.impl.integration;

import java.math.BigDecimal;

public class SimpleMain {

	public static void main(String[] args) {
		System.out.println(new BigDecimal("0.0318").multiply(new BigDecimal("100")).setScale(2).toString()+"%");
	}

}
