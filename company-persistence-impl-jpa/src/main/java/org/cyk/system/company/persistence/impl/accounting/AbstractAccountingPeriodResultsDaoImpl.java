package org.cyk.system.company.persistence.impl.accounting;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.NoResultException;

import org.cyk.system.company.model.accounting.AbstractAccountingPeriodResults;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.persistence.api.accounting.AbstractAccountingPeriodResultsDao;
import org.cyk.system.root.model.AbstractEnumeration;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;
import org.cyk.system.root.persistence.impl.QueryWrapper;
import org.cyk.utility.common.computation.ArithmeticOperator;

public abstract class AbstractAccountingPeriodResultsDaoImpl<RESULTS extends AbstractAccountingPeriodResults<ENTITY>,ENTITY extends AbstractEnumeration> extends AbstractTypedDao<RESULTS> implements AbstractAccountingPeriodResultsDao<RESULTS,ENTITY>, Serializable {

	private static final long serialVersionUID = 7904009035909460023L;

	protected static final String SELECT_NUMBER_OF_SALES_FORMAT = 
			"SELECT record.saleResults.cost.numberOfProceedElements FROM %s record WHERE record.accountingPeriod = :accountingPeriod AND record.entity.identifier IN :identifiers "
			+ "ORDER BY record.saleResults.cost.numberOfProceedElements %s";
	
	protected String readByAccountingPeriodByEntity,readByAccountingPeriod,readByAccountingPeriodByEntities,readByEntity;
	protected String readHighestNumberOfSales,readLowestNumberOfSales,readByAccountingPeriodByEntityCategoriesByNumberOfSales;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByAccountingPeriodByEntity, _select().where(AbstractAccountingPeriodResults.FIELD_ACCOUNTING_PERIOD)
				.and(AbstractAccountingPeriodResults.FIELD_ENTITY));
		registerNamedQuery(readByAccountingPeriodByEntities, _select().whereIdentifierIn(AbstractAccountingPeriodResults.FIELD_ENTITY)
				.and(AbstractAccountingPeriodResults.FIELD_ACCOUNTING_PERIOD).orderBy("saleResults.cost.numberOfProceedElements", Boolean.FALSE));
		registerNamedQuery(readByAccountingPeriod, _select().where(AbstractAccountingPeriodResults.FIELD_ACCOUNTING_PERIOD));
		registerNamedQuery(readByEntity, _select().where(AbstractAccountingPeriodResults.FIELD_ENTITY));
		
		registerNamedQuery(readHighestNumberOfSales, String.format(SELECT_NUMBER_OF_SALES_FORMAT ,clazz.getSimpleName(),"DESC"));
		registerNamedQuery(readLowestNumberOfSales, String.format(SELECT_NUMBER_OF_SALES_FORMAT, clazz.getSimpleName(),"ASC"));
		registerNamedQuery(readByAccountingPeriodByEntityCategoriesByNumberOfSales, _select().whereIdentifierIn(AbstractAccountingPeriodResults.FIELD_ENTITY)
				.and(AbstractAccountingPeriodResults.FIELD_ACCOUNTING_PERIOD)
				.and("saleResults.cost.numberOfProceedElements","numberOfSales",ArithmeticOperator.EQ));
	}
	
	@Override
	public RESULTS readByAccountingPeriodByEntity(AccountingPeriod accountingPeriod, ENTITY product) {
		return namedQuery(readByAccountingPeriodByEntity).ignoreThrowable(NoResultException.class)
				.parameter(AbstractAccountingPeriodResults.FIELD_ACCOUNTING_PERIOD, accountingPeriod).parameter(AbstractAccountingPeriodResults.FIELD_ENTITY, product)
				.resultOne();
	}
	
	@Override
	public Collection<RESULTS> readByAccountingPeriodByEntities(AccountingPeriod accountingPeriod, Collection<ENTITY> products) {
		return namedQuery(readByAccountingPeriodByEntities).parameterIdentifiers(products).parameter(AbstractAccountingPeriodResults.FIELD_ACCOUNTING_PERIOD
				, accountingPeriod).resultMany();
	}

	@Override
	public Collection<RESULTS> readByAccountingPeriod(AccountingPeriod accountingPeriod) {
		return namedQuery(readByAccountingPeriod).parameter(AbstractAccountingPeriodResults.FIELD_ACCOUNTING_PERIOD, accountingPeriod).resultMany();
	}
	
	@Override
	public Collection<RESULTS> readByEntity(ENTITY product) {
		return namedQuery(readByEntity).parameter(AbstractAccountingPeriodResults.FIELD_ENTITY, product).resultMany();
	}
	
	private BigDecimal extremeNumberOfSales(AccountingPeriod accountingPeriod,Collection<ENTITY> products,Boolean highest) {
		QueryWrapper<BigDecimal> queryWrapper =  namedQuery(Boolean.TRUE.equals(highest)?readHighestNumberOfSales:readLowestNumberOfSales, BigDecimal.class)
				.parameter(AbstractAccountingPeriodResults.FIELD_ACCOUNTING_PERIOD, accountingPeriod).parameterIdentifiers(products);
		queryWrapper.getReadConfig().setMaximumResultCount(1l);
		Collection<BigDecimal> values = queryWrapper.resultMany();
		return values.isEmpty()?BigDecimal.ZERO:values.iterator().next();
	}
	
	@Override
	public BigDecimal readHighestNumberOfSales(AccountingPeriod accountingPeriod,Collection<ENTITY> products) {
		return extremeNumberOfSales(accountingPeriod, products, Boolean.TRUE);
	}

	@Override
	public BigDecimal readLowestNumberOfSales(AccountingPeriod accountingPeriod,Collection<ENTITY> products) {
		return extremeNumberOfSales(accountingPeriod, products, Boolean.FALSE);
	}

	@Override
	public Collection<RESULTS> readByAccountingPeriodByEntitiesByNumberOfSales(AccountingPeriod accountingPeriod
			,Collection<ENTITY> products,BigDecimal numberOfSales) {
		return namedQuery(readByAccountingPeriodByEntityCategoriesByNumberOfSales).parameter(AbstractAccountingPeriodResults.FIELD_ACCOUNTING_PERIOD, accountingPeriod)
				.parameter("numberOfSales", numberOfSales)
		.parameterIdentifiers(products).resultMany();
	}

}
