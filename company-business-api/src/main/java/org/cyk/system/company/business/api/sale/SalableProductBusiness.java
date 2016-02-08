package org.cyk.system.company.business.api.sale;

import java.util.List;

import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.root.business.api.TypedBusiness;

public interface SalableProductBusiness extends TypedBusiness<SalableProduct> {

	SalableProduct instanciateOne(String productCode,String unitPrice);
	List<SalableProduct> instanciateMany(String[][] arguments);
	
}
