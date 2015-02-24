package org.cyk.system.company.business.api.product;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.product.Payment;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.root.business.api.TypedBusiness;

public interface PaymentBusiness extends TypedBusiness<Payment> {

	Collection<Payment> findBySale(Sale sale);

	void in(Payment payment);
	void out(Payment payment);
	
	Payment create(Payment payment,Boolean payback);
	
	BigDecimal computeBalance(Payment payment);
}
