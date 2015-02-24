package org.cyk.system.company.business.api.product;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.product.Payment;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.company.model.product.SaledProduct;
import org.cyk.system.root.business.api.TypedBusiness;

public interface SaleBusiness extends TypedBusiness<Sale> {

	SaledProduct selectProduct(Sale sale,Product product);
	void unselectProduct(Sale sale,SaledProduct saledProduct);
	void quantifyProduct(Sale sale,SaledProduct saledProduct);

	void create(Sale sale,Payment payment);
	
	Collection<Sale> findByCriteria(SaleSearchCriteria criteria);
	Long countByCriteria(SaleSearchCriteria criteria);
	
	BigDecimal sumCostByCriteria(SaleSearchCriteria criteria);
	BigDecimal sumBalanceByCriteria(SaleSearchCriteria criteria);
	
}
