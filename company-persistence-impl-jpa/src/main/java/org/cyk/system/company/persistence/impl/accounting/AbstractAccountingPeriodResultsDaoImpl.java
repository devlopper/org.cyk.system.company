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

public abstract class AbstractAccountingPeriodResultsDaoImpl<RESULTS extends AbstractAccountingPeriodResults<PRODUCT>,PRODUCT extends AbstractEnumeration> extends AbstractTypedDao<RESULTS> implements AbstractAccountingPeriodResultsDao<RESULTS,PRODUCT>, Serializable {

	private static final long serialVersionUID = 7904009035909460023L;

	protected static final String SELECT_NUMBER_OF_SALES_FORMAT = 
			"SELECT record.salesResults.count FROM %s record WHERE record.accountingPeriod = :accountingPeriod AND record.entity.identifier IN :identifiers "
			+ "ORDER BY record.salesResults.count %s";
	
	protected String readByAccountingPeriodByProduct,readByAccountingPeriod,readByAccountingPeriodByProducts;
	protected String readHighestNumberOfSales,readLowestNumberOfSales,readByAccountingPeriodByProductCategoriesByNumberOfSales;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByAccountingPeriodByProduct, _select().where("accountingPeriod").and("entity"));
		registerNamedQuery(readByAccountingPeriodByProducts, _select().whereIdentifierIn("entity").and("accountingPeriod").orderBy("salesResults.count", Boolean.FALSE));
		registerNamedQuery(readByAccountingPeriod, _select().where("accountingPeriod"));
		
		registerNamedQuery(readHighestNumberOfSales, String.format(SELECT_NUMBER_OF_SALES_FORMAT ,clazz.getSimpleName(),"DESC"));
		registerNamedQuery(readLowestNumberOfSales, String.format(SELECT_NUMBER_OF_SALES_FORMAT, clazz.getSimpleName(),"ASC"));
		registerNamedQuery(readByAccountingPeriodByProductCategoriesByNumberOfSales, _select().whereIdentifierIn("entity").and("accountingPeriod")
				.and("salesResults.count","numberOfSales",ArithmeticOperator.EQ));
	}
	
	@Override
	public RESULTS readByAccountingPeriodByProduct(AccountingPeriod accountingPeriod, PRODUCT product) {
		return namedQuery(readByAccountingPeriodByProduct).ignoreThrowable(NoResultException.class)
				.parameter("accountingPeriod", accountingPeriod).parameter("entity", product).resultOne();
	}
	
	@Override
	public Collection<RESULTS> readByAccountingPeriodByProducts(AccountingPeriod accountingPeriod, Collection<PRODUCT> products) {
		return namedQuery(readByAccountingPeriodByProducts).parameterIdentifiers(products).parameter("accountingPeriod", accountingPeriod).resultMany();
	}

	@Override
	public Collection<RESULTS> readByAccountingPeriod(AccountingPeriod accountingPeriod) {
		return namedQuery(readByAccountingPeriod)
				.parameter("accountingPeriod", accountingPeriod).resultMany();
	}
	
	private BigDecimal extremeNumberOfSales(AccountingPeriod accountingPeriod,Collection<PRODUCT> products,Boolean highest) {
		QueryWrapper<BigDecimal> queryWrapper =  namedQuery(Boolean.TRUE.equals(highest)?readHighestNumberOfSales:readLowestNumberOfSales, BigDecimal.class)
				.parameter("accountingPeriod", accountingPeriod).parameterIdentifiers(products);
		queryWrapper.getReadConfig().setMaximumResultCount(1l);
		Collection<BigDecimal> values = queryWrapper.resultMany();
		return values.isEmpty()?BigDecimal.ZERO:values.iterator().next();
	}
	
	@Override
	public BigDecimal readHighestNumberOfSales(AccountingPeriod accountingPeriod,Collection<PRODUCT> products) {
		return extremeNumberOfSales(accountingPeriod, products, Boolean.TRUE);
	}

	@Override
	public BigDecimal readLowestNumberOfSales(AccountingPeriod accountingPeriod,Collection<PRODUCT> products) {
		return extremeNumberOfSales(accountingPeriod, products, Boolean.FALSE);
	}

	@Override
	public Collection<RESULTS> readByAccountingPeriodByProductsByNumberOfSales(AccountingPeriod accountingPeriod
			,Collection<PRODUCT> products,BigDecimal numberOfSales) {
		return namedQuery(readByAccountingPeriodByProductCategoriesByNumberOfSales).parameter("accountingPeriod", accountingPeriod).parameter("numberOfSales", numberOfSales)
		.parameterIdentifiers(products).resultMany();
	}

}
