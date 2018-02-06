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
		addInstances(TangibleProduct.class, inject(TangibleProductBusiness.class).instanciateOne(TANGIBLE_PRODUCT_TP2));
		addInstances(TangibleProduct.class, inject(TangibleProductBusiness.class).instanciateOne(TANGIBLE_PRODUCT_TP3));
		addInstances(TangibleProduct.class, inject(TangibleProductBusiness.class).instanciateOne(TANGIBLE_PRODUCT_TP4));
		addInstances(TangibleProduct.class, inject(TangibleProductBusiness.class).instanciateOne(TANGIBLE_PRODUCT_TP5));
		
    	addInstances(SalableProduct.class, inject(SalableProductBusiness.class).instanciateOne().setProduct(InstanceHelper.Pool.getInstance()
    			.get(TangibleProduct.class, TANGIBLE_PRODUCT_TP1)).setPrice(new BigDecimal("100")));
    	
    	addInstances(SalableProduct.class, inject(SalableProductBusiness.class).instanciateOne().setProduct(InstanceHelper.Pool.getInstance()
    			.get(TangibleProduct.class, TANGIBLE_PRODUCT_TP2)).setPrice(new BigDecimal("75")));
    	
    	addInstances(SalableProduct.class, inject(SalableProductBusiness.class).instanciateOne().setProduct(InstanceHelper.Pool.getInstance()
    			.get(TangibleProduct.class, TANGIBLE_PRODUCT_TP3)).setPrice(new BigDecimal("150")));
    	
    	addInstances(SalableProduct.class, inject(SalableProductBusiness.class).instanciateOne().setProduct(InstanceHelper.Pool.getInstance()
    			.get(TangibleProduct.class, TANGIBLE_PRODUCT_TP4)).setPrice(new BigDecimal("500")));
    	
    	addInstances(SalableProduct.class, inject(SalableProductBusiness.class).instanciateOne().setProduct(InstanceHelper.Pool.getInstance()
    			.get(TangibleProduct.class, TANGIBLE_PRODUCT_TP5)).setPrice(new BigDecimal("1000")));
    	
	}
	
	/**/
	/**
	 * Price = 100
	 */
	public static final String TANGIBLE_PRODUCT_TP1 = RandomHelper.getInstance().getAlphabetic(4);
	/**
	 * Price = 75
	 */
	public static final String TANGIBLE_PRODUCT_TP2 = RandomHelper.getInstance().getAlphabetic(4);
	/**
	 * Price = 150
	 */
	public static final String TANGIBLE_PRODUCT_TP3 = RandomHelper.getInstance().getAlphabetic(4);
	/**
	 * Price = 500
	 */
	public static final String TANGIBLE_PRODUCT_TP4 = RandomHelper.getInstance().getAlphabetic(4);
	/**
	 * Price = 1000
	 */
	public static final String TANGIBLE_PRODUCT_TP5 = RandomHelper.getInstance().getAlphabetic(4);
	
	//public static final BigDecimal SALABLE_PRODUCT_TP1 = RandomHelper.getInstance().getAlphabetic(4);
}
