package org.cyk.system.company.business.impl.__data__;

import java.io.Serializable;
import java.math.BigDecimal;

import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.utility.common.helper.RandomHelper;
@Deprecated
public class FakedDataSet extends RealDataSet implements Serializable {
	private static final long serialVersionUID = -2798230163660365442L;

	public FakedDataSet() {
		super();
		addProductInstances();
	}

	protected void addProductInstances(){
		addInstances(TangibleProduct.class, inject(TangibleProductBusiness.class).instanciateOne(TANGIBLE_PRODUCT_TP1,"TP 001"));
		addInstances(TangibleProduct.class, inject(TangibleProductBusiness.class).instanciateOne(TANGIBLE_PRODUCT_TP2,"TP 002"));
		addInstances(TangibleProduct.class, inject(TangibleProductBusiness.class).instanciateOne(TANGIBLE_PRODUCT_TP3,"TP 003"));
		addInstances(TangibleProduct.class, inject(TangibleProductBusiness.class).instanciateOne(TANGIBLE_PRODUCT_TP4,"TP 004"));
		addInstances(TangibleProduct.class, inject(TangibleProductBusiness.class).instanciateOne(TANGIBLE_PRODUCT_TP5,"TP 005"));
		
		addInstances(TangibleProduct.class, inject(TangibleProductBusiness.class).instanciateOne(TANGIBLE_PRODUCT_NO_SALABLE_TP1,"TP 001"));
		addInstances(TangibleProduct.class, inject(TangibleProductBusiness.class).instanciateOne(TANGIBLE_PRODUCT_NO_SALABLE_TP2,"TP 002"));
		addInstances(TangibleProduct.class, inject(TangibleProductBusiness.class).instanciateOne(TANGIBLE_PRODUCT_NO_SALABLE_TP3,"TP 003"));
		addInstances(TangibleProduct.class, inject(TangibleProductBusiness.class).instanciateOne(TANGIBLE_PRODUCT_NO_SALABLE_TP4,"TP 004"));
		addInstances(TangibleProduct.class, inject(TangibleProductBusiness.class).instanciateOne(TANGIBLE_PRODUCT_NO_SALABLE_TP5,"TP 005"));
		
		addInstances(TangibleProduct.class, inject(TangibleProductBusiness.class).instanciateOne(TANGIBLE_PRODUCT_NO_STOCKABLE_TP1,"TP 001"));
		addInstances(TangibleProduct.class, inject(TangibleProductBusiness.class).instanciateOne(TANGIBLE_PRODUCT_NO_STOCKABLE_TP2,"TP 002"));
		addInstances(TangibleProduct.class, inject(TangibleProductBusiness.class).instanciateOne(TANGIBLE_PRODUCT_NO_STOCKABLE_TP3,"TP 003"));
		addInstances(TangibleProduct.class, inject(TangibleProductBusiness.class).instanciateOne(TANGIBLE_PRODUCT_NO_STOCKABLE_TP4,"TP 004"));
		addInstances(TangibleProduct.class, inject(TangibleProductBusiness.class).instanciateOne(TANGIBLE_PRODUCT_NO_STOCKABLE_TP5,"TP 005"));
		/*
		addInstances(StockableProduct.class, inject(StockableProductBusiness.class).instanciateOne()
				.setTangibleProduct(getInstance(TangibleProduct.class, TANGIBLE_PRODUCT_TP1)).setQuantityMovementCollectionValue(new BigDecimal("10")));
		addInstances(StockableProduct.class, inject(StockableProductBusiness.class).instanciateOne()
				.setTangibleProduct(getInstance(TangibleProduct.class, TANGIBLE_PRODUCT_TP2)).setQuantityMovementCollectionValue(new BigDecimal("10")));
		addInstances(StockableProduct.class, inject(StockableProductBusiness.class).instanciateOne()
				.setTangibleProduct(getInstance(TangibleProduct.class, TANGIBLE_PRODUCT_TP3)).setQuantityMovementCollectionValue(new BigDecimal("10")));
		addInstances(StockableProduct.class, inject(StockableProductBusiness.class).instanciateOne()
				.setTangibleProduct(getInstance(TangibleProduct.class, TANGIBLE_PRODUCT_TP4)).setQuantityMovementCollectionValue(new BigDecimal("10")));
		addInstances(StockableProduct.class, inject(StockableProductBusiness.class).instanciateOne()
				.setTangibleProduct(getInstance(TangibleProduct.class, TANGIBLE_PRODUCT_TP5)).setQuantityMovementCollectionValue(new BigDecimal("10")));
		*/
    	addInstances(SalableProduct.class, inject(SalableProductBusiness.class).instanciateOne().setProduct(getInstance(TangibleProduct.class, TANGIBLE_PRODUCT_TP1))
    			.setPrice(new BigDecimal("100")));    	
    	addInstances(SalableProduct.class, inject(SalableProductBusiness.class).instanciateOne().setProduct(getInstance(TangibleProduct.class, TANGIBLE_PRODUCT_TP2))
    			.setPrice(new BigDecimal("75")));    	
    	addInstances(SalableProduct.class, inject(SalableProductBusiness.class).instanciateOne().setProduct(getInstance(TangibleProduct.class, TANGIBLE_PRODUCT_TP3))
    			.setPrice(new BigDecimal("150")));    	
    	addInstances(SalableProduct.class, inject(SalableProductBusiness.class).instanciateOne().setProduct(getInstance(TangibleProduct.class, TANGIBLE_PRODUCT_TP4))
    			.setPrice(new BigDecimal("500")));    	
    	addInstances(SalableProduct.class, inject(SalableProductBusiness.class).instanciateOne().setProduct(getInstance(TangibleProduct.class, TANGIBLE_PRODUCT_TP5))
    			.setPrice(new BigDecimal("1000")));
    	
	}
	
	/**/
	/**
	 * Price = 100 , Quantity = 10
	 */
	public static final String TANGIBLE_PRODUCT_TP1 = RandomHelper.getInstance().getAlphabetic(4);
	/**
	 * Price = 75 , Quantity = 10
	 */
	public static final String TANGIBLE_PRODUCT_TP2 = RandomHelper.getInstance().getAlphabetic(4);
	/**
	 * Price = 150 , Quantity = 10
	 */
	public static final String TANGIBLE_PRODUCT_TP3 = RandomHelper.getInstance().getAlphabetic(4);
	/**
	 * Price = 500 , Quantity = 10
	 */
	public static final String TANGIBLE_PRODUCT_TP4 = RandomHelper.getInstance().getAlphabetic(4);
	/**
	 * Price = 1000 , Quantity = 10
	 */
	public static final String TANGIBLE_PRODUCT_TP5 = RandomHelper.getInstance().getAlphabetic(4);
	
	public static final String TANGIBLE_PRODUCT_NO_SALABLE_TP1 = RandomHelper.getInstance().getAlphabetic(4);
	public static final String TANGIBLE_PRODUCT_NO_SALABLE_TP2 = RandomHelper.getInstance().getAlphabetic(4);
	public static final String TANGIBLE_PRODUCT_NO_SALABLE_TP3 = RandomHelper.getInstance().getAlphabetic(4);
	public static final String TANGIBLE_PRODUCT_NO_SALABLE_TP4 = RandomHelper.getInstance().getAlphabetic(4);
	public static final String TANGIBLE_PRODUCT_NO_SALABLE_TP5 = RandomHelper.getInstance().getAlphabetic(4);
	
	public static final String TANGIBLE_PRODUCT_NO_STOCKABLE_TP1 = RandomHelper.getInstance().getAlphabetic(4);
	public static final String TANGIBLE_PRODUCT_NO_STOCKABLE_TP2 = RandomHelper.getInstance().getAlphabetic(4);
	public static final String TANGIBLE_PRODUCT_NO_STOCKABLE_TP3 = RandomHelper.getInstance().getAlphabetic(4);
	public static final String TANGIBLE_PRODUCT_NO_STOCKABLE_TP4 = RandomHelper.getInstance().getAlphabetic(4);
	public static final String TANGIBLE_PRODUCT_NO_STOCKABLE_TP5 = RandomHelper.getInstance().getAlphabetic(4);
	
	//public static final BigDecimal SALABLE_PRODUCT_TP1 = RandomHelper.getInstance().getAlphabetic(4);
}
