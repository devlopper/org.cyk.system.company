package org.cyk.system.company.persistence.impl.product;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.product.Payment;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.persistence.api.product.PaymentDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class PaymentDaoImpl extends AbstractTypedDao<Payment> implements PaymentDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readBySale,sumAmountPaid/*,readAmountToPay*/;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readBySale, _select().where("sale"));
		registerNamedQuery(sumAmountPaid, "SELECT SUM(payment.amountPaid) FROM Payment payment WHERE payment.sale = :sale");
		//registerNamedQuery(readAmountToPay, "SELECT SUM(payment.amountPaid) FROM Payment payment WHERE payment.sale = :sale");
	}
	
	@Override
	public Collection<Payment> readBySale(Sale sale) {
		return namedQuery(readBySale).parameter("sale", sale).resultMany();
	}

	@Override
	public BigDecimal sumAmountPaid(Sale sale) {
		return namedQuery(sumAmountPaid,BigDecimal.class).parameter("sale", sale).nullValue(BigDecimal.ZERO).resultOne();
	}
	
	/*
	@Override
	public BigDecimal readAmountToPay(Sale sale) {
		return namedQuery(readAmountToPay, BigDecimal.class).parameter("sale", sale).resultOne();
	}
	*/

}
