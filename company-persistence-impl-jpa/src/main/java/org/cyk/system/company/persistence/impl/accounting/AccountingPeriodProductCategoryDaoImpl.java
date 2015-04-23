package org.cyk.system.company.persistence.impl.accounting;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.accounting.AccountingPeriodProductCategory;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodProductCategoryDao;
import org.cyk.utility.common.computation.ArithmeticOperator;

public class AccountingPeriodProductCategoryDaoImpl extends AbstractAccountingPeriodResultsDaoImpl<AccountingPeriodProductCategory,ProductCategory> implements AccountingPeriodProductCategoryDao, Serializable {

	private static final long serialVersionUID = 7904009035909460023L;

	private String readByProductCategories;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByProductCategories, _select().where("accountingPeriod").and("entity.identifier", "identifiers", ArithmeticOperator.IN));	
	}
	
	@Override
	public Collection<AccountingPeriodProductCategory> readByAccountingPeriodByProductCategories(AccountingPeriod accountingPeriod,Collection<ProductCategory> productCategories) {
		return namedQuery(readByProductCategories).parameter("accountingPeriod", accountingPeriod).parameterIdentifiers(productCategories).resultMany();
	}


}
