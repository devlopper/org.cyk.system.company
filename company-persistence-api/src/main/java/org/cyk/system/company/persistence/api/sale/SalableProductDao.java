package org.cyk.system.company.persistence.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.root.persistence.api.AbstractCollectionDao;

public interface SalableProductDao extends AbstractCollectionDao<SalableProduct,SalableProductInstance> {
	
	SalableProduct readByProduct(Product product);
	Collection<SalableProduct> readByCashRegister(CashRegister cashRegister);
	
}
