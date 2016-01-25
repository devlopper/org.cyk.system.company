package org.cyk.system.company.business.api.sale;

import java.util.List;

import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.root.business.api.TypedBusiness;

public interface SalableProductBusiness extends TypedBusiness<SalableProduct> {

	SalableProduct instanciate(String productCode,String unitPrice);
	List<SalableProduct> instanciate(String[][] arguments);
	
}
