package org.cyk.system.company.business.api.sale;

import java.util.List;

import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.root.business.api.AbstractCollectionBusiness;

public interface SalableProductBusiness extends AbstractCollectionBusiness<SalableProduct,SalableProductInstance> {

	SalableProduct instanciateOne(String productCode,String unitPrice);
	List<SalableProduct> instanciateMany(String[][] arguments);
	
}
