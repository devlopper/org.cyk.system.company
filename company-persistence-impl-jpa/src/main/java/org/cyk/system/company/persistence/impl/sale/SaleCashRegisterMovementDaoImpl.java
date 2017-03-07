package org.cyk.system.company.persistence.impl.sale;

import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.NoResultException;

import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementDao;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.persistence.impl.AbstractCollectionItemDaoImpl;
import org.cyk.system.root.persistence.impl.QueryStringBuilder;

public class SaleCashRegisterMovementDaoImpl extends AbstractCollectionItemDaoImpl<SaleCashRegisterMovement,SaleCashRegisterMovementCollection> implements SaleCashRegisterMovementDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readBySale,countBySale,sumAmount,readByCashRegisterMovementCode,readBySupportingDocumentIdentifiers;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readBySale, _select().where(SaleCashRegisterMovement.FIELD_SALE));
		registerNamedQuery(sumAmount, "SELECT SUM(scrm.amount) FROM SaleCashRegisterMovement scrm WHERE scrm.sale = :sale");
		registerNamedQuery(readBySupportingDocumentIdentifiers, "SELECT r FROM SaleCashRegisterMovement r WHERE r.collection.cashRegisterMovement.movement.supportingDocumentIdentifier IN :identifiers");
		registerNamedQuery(readByCashRegisterMovementCode, _select().where(commonUtils
				.attributePath(SaleCashRegisterMovement.FIELD_COLLECTION,SaleCashRegisterMovementCollection.FIELD_CASH_REGISTER_MOVEMENT, CashRegisterMovement.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_CODE),GlobalIdentifier.FIELD_CODE));
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
	public SaleCashRegisterMovement readByCashRegisterMovementCode(String code) {
		return namedQuery(readByCashRegisterMovementCode).parameter(GlobalIdentifier.FIELD_CODE, code).ignoreThrowable(NoResultException.class).resultOne();
	}
	
	@Override
	public Collection<SaleCashRegisterMovement> readBySupportingDocumentIdentifiers(Collection<String> supportingDocumentIdentifiers) {
		return namedQuery(readBySupportingDocumentIdentifiers).parameter(QueryStringBuilder.VAR_IDENTIFIERS, supportingDocumentIdentifiers).resultMany();
	}

}
