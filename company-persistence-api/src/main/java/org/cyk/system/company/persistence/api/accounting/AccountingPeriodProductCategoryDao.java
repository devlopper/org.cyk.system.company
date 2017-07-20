package org.cyk.system.company.persistence.api.accounting;

import java.util.Collection;

import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.accounting.AccountingPeriodProductCategory;
import org.cyk.system.company.model.product.ProductCategory;

public interface AccountingPeriodProductCategoryDao extends AbstractAccountingPeriodResultsDao<AccountingPeriodProductCategory,ProductCategory> {

	Collection<AccountingPeriodProductCategory> readByAccountingPeriodByProductCategories(AccountingPeriod accountingPeriod,Collection<ProductCategory> productCategories);

	
}
