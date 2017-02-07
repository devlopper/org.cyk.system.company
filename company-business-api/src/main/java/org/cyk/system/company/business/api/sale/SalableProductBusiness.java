package org.cyk.system.company.business.api.sale;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.root.business.api.AbstractCollectionBusiness;

public interface SalableProductBusiness extends AbstractCollectionBusiness<SalableProduct,SalableProductInstance> {

	void create(Class<? extends Product> aClass,String code,String name,BigDecimal price);

	Collection<SalableProduct> findByCashRegister(CashRegister cashRegister);
}
