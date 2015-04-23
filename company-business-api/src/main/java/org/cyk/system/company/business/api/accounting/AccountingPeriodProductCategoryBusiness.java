package org.cyk.system.company.business.api.accounting;

import java.util.Collection;

import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.accounting.AccountingPeriodProductCategory;
import org.cyk.system.company.model.product.ProductCategory;

public interface AccountingPeriodProductCategoryBusiness extends AbstractAccountingPeriodResultsBusiness<AccountingPeriodProductCategory,ProductCategory> {

	Collection<AccountingPeriodProductCategory> findHierarchiesByAccountingPeriod(AccountingPeriod accountingPeriod);
	Collection<AccountingPeriodProductCategory> findHierarchies();
	
	Collection<AccountingPeriodProductCategory> findHierarchiesByAccountingPeriod(AccountingPeriod accountingPeriod,Collection<ProductCategory> productCategories);
	Collection<AccountingPeriodProductCategory> findHierarchies(Collection<ProductCategory> productCategories);

	//Collection<AccountingPeriodProductCategory> findHighestNumberOfSales(AccountingPeriod accountingPeriod, Collection<ProductCategory> productCategories);
	//Collection<AccountingPeriodProductCategory> findLowestNumberOfSales(AccountingPeriod accountingPeriod, Collection<ProductCategory> productCategories);
	
	//AccountingPeriodProductCategory findHighestNumberOfSalesByCategory(Pro);
	//AccountingPeriodProductCategory findLowestNumberOfSalesCategory();
	
	//AccountingPeriodProductCategory findByCategoryByRank();
}
