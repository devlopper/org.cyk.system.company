package org.cyk.system.company.persistence.impl.sale;

import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.NoResultException;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementDao;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.persistence.impl.AbstractCollectionItemDaoImpl;
import org.cyk.system.root.persistence.impl.QueryStringBuilder;
import org.cyk.system.root.persistence.impl.QueryWrapper;

@Deprecated
public class SaleCashRegisterMovementDaoImpl extends AbstractCollectionItemDaoImpl<SaleCashRegisterMovement,SaleCashRegisterMovementCollection> implements SaleCashRegisterMovementDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readBySale,countBySale,sumAmount,readByCashRegisterMovementCode;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readBySale, _select().where(SaleCashRegisterMovement.FIELD_SALE));
		registerNamedQuery(sumAmount, "SELECT SUM(scrm.amount) FROM SaleCashRegisterMovement scrm WHERE scrm.sale = :sale");
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
	public BigDecimal sumAmountBySale(Sale sale) { 
		return namedQuery(sumAmount,BigDecimal.class).parameter(SaleCashRegisterMovement.FIELD_SALE, sale).nullValue(BigDecimal.ZERO).resultOne();
	}

	@Override
	public SaleCashRegisterMovement readByCashRegisterMovementCode(String code) {
		return namedQuery(readByCashRegisterMovementCode).parameter(GlobalIdentifier.FIELD_CODE, code).ignoreThrowable(NoResultException.class).resultOne();
	}	
	
	@Override
	protected void processQueryStringBuilder(QueryStringBuilder queryStringBuilder, String queryName) {
		super.processQueryStringBuilder(queryStringBuilder, queryName);
		if(ArrayUtils.contains(new String[]{readWhereExistencePeriodFromDateIsLessThan,countWhereExistencePeriodFromDateIsLessThan
				,readWhereExistencePeriodFromDateIsGreaterThan,countWhereExistencePeriodFromDateIsGreaterThan}, queryName)){
			queryStringBuilder.and(SaleCashRegisterMovement.FIELD_SALE);
		}
	}
		
	@Override
	protected <T> void processQueryWrapper(Class<T> aClass,QueryWrapper<T> queryWrapper, String queryName,Object[] arguments) {
		super.processQueryWrapper(aClass, queryWrapper, queryName,arguments);
		if(ArrayUtils.contains(new String[]{readWhereExistencePeriodFromDateIsLessThan,countWhereExistencePeriodFromDateIsLessThan
				,readWhereExistencePeriodFromDateIsGreaterThan,countWhereExistencePeriodFromDateIsGreaterThan}, queryName)){
			SaleCashRegisterMovement saleCashRegisterMovement = (SaleCashRegisterMovement) arguments[0];
			queryWrapper.parameter(SaleCashRegisterMovement.FIELD_SALE, saleCashRegisterMovement.getSale());
		}
	}

}
