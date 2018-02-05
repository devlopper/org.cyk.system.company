package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;

import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.utility.common.helper.InstanceHelper;
import org.cyk.utility.common.helper.RandomHelper;

public class FakedDataSet extends RealDataSet implements Serializable {
	private static final long serialVersionUID = -2798230163660365442L;

	
	
	public FakedDataSet() {
		super();
		
		addProductInstances();
		
	}

	protected void addProductInstances(){
		addInstances(TangibleProduct.class, inject(TangibleProductBusiness.class).instanciateOne(TANGIBLE_PRODUCT_TP1));
		
		SalableProduct salableProduct = inject(SalableProductBusiness.class).instanciateOne();
    	salableProduct.setProduct(InstanceHelper.Pool.getInstance().get(TangibleProduct.class, TANGIBLE_PRODUCT_TP1));
    	salableProduct.setPrice(new BigDecimal("100"));
    	addInstances(SalableProduct.class, salableProduct);
	}
	
	/**/
	
	public static final String TANGIBLE_PRODUCT_TP1 = RandomHelper.getInstance().getAlphabetic(4);
	
	//public static final BigDecimal SALABLE_PRODUCT_TP1 = RandomHelper.getInstance().getAlphabetic(4);
}
