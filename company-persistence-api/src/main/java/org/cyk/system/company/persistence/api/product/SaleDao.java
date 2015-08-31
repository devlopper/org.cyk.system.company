package org.cyk.system.company.persistence.api.product;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.root.persistence.api.TypedDao;

public interface SaleDao extends TypedDao<Sale> {

	Collection<Sale> readByCriteria(SaleSearchCriteria criteria);
	
	Long countByCriteria(SaleSearchCriteria criteria);

	BigDecimal sumCostByCriteria(SaleSearchCriteria criteria);
	
	BigDecimal sumBalanceByCriteria(SaleSearchCriteria criteria);

	BigDecimal sumValueAddedTaxByCriteria(SaleSearchCriteria criteria);
	
	//BigDecimal sumBalanceByCustomer(SaleSearchCriteria criteria);
	
	Sale readByComputedIdentifier(String computedIdentifier);
	
	/*
	Collection<Sale> readByCustomer(Customer customer,Collection<BalanceType> balanceTypes);
	Long countByCustomer(Customer customer,Collection<BalanceType> balanceTypes);
	*/
}
