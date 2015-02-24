package org.cyk.system.company.business.api;

import org.cyk.system.company.model.product.Payment;
import org.cyk.system.company.model.product.Sale;

public interface CompanyValueGenerator {

	String saleIdentificationNumber(Sale sale);
	
	String paymentIdentificationNumber(Payment payment);
	
}
