package org.cyk.system.company.persistence.api.product;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.product.Payment;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.root.persistence.api.TypedDao;

public interface PaymentDao extends TypedDao<Payment> {

	Collection<Payment> readBySale(Sale sale);
	
	BigDecimal sumAmountPaid(Sale sale);
	
	//BigDecimal readAmountToPay(Sale sale);

}
