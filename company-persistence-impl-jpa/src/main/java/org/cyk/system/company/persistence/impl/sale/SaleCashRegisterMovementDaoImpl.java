package org.cyk.system.company.persistence.impl.sale;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class SaleCashRegisterMovementDaoImpl extends AbstractTypedDao<SaleCashRegisterMovement> implements SaleCashRegisterMovementDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readBySale,countBySale,sumAmount;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readBySale, _select().where(SaleCashRegisterMovement.FIELD_SALE));
		registerNamedQuery(sumAmount, "SELECT SUM(scrm.cashRegisterMovement.movement.value) FROM SaleCashRegisterMovement scrm WHERE scrm.sale = :sale");
	}
	
	@Override
	public Collection<SaleCashRegisterMovement> readBySale(Sale sale) {
		return namedQuery(readBySale).parameter(SaleCashRegisterMovement.FIELD_SALE, sale).resultMany();
	}
	 
	@Override
	public Long countBySale(Sale sale) {
		return countNamedQuery(countBySale).parameter(SaleCashRegisterMovement.FIELD_SALE, sale).resultOne();
	}

	@Override
	public BigDecimal sumAmount(Sale sale) { 
		return namedQuery(sumAmount,BigDecimal.class).parameter(SaleCashRegisterMovement.FIELD_SALE, sale).nullValue(BigDecimal.ZERO).resultOne();
	}

	

}
