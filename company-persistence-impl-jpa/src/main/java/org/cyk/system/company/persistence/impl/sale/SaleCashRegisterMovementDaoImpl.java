package org.cyk.system.company.persistence.impl.sale;

import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.NoResultException;

import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class SaleCashRegisterMovementDaoImpl extends AbstractTypedDao<SaleCashRegisterMovement> implements SaleCashRegisterMovementDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readBySale,countBySale,sumAmount,readByCashRegisterMovementComputedIdentifier;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readBySale, _select().where(SaleCashRegisterMovement.FIELD_SALE));
		registerNamedQuery(sumAmount, "SELECT SUM(scrm.cashRegisterMovement.movement.value) FROM SaleCashRegisterMovement scrm WHERE scrm.sale = :sale");
		registerNamedQuery(readByCashRegisterMovementComputedIdentifier, _select().where(commonUtils
				.attributePath(SaleCashRegisterMovement.FIELD_CASH_REGISTER_MOVEMENT, CashRegisterMovement.FIELD_COMPUTED_IDENTIFIER),CashRegisterMovement.FIELD_COMPUTED_IDENTIFIER));
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

	@Override
	public SaleCashRegisterMovement readByCashRegisterMovementComputedIdentifier(String identifier) {
		return namedQuery(readByCashRegisterMovementComputedIdentifier).parameter(CashRegisterMovement.FIELD_COMPUTED_IDENTIFIER, identifier).ignoreThrowable(NoResultException.class).resultOne();
	}

}
