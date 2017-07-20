package org.cyk.system.company.persistence.impl.accounting;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.utility.common.cdi.AbstractBean;

public class CompanyPersistenceTestHelper extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 2499068990390871628L;

	@Inject private ProductDao productDao;
	
	public void set(Product product,String code){
		product.setCode(code);
		product.setName(code);
	}
	
	public void set(SalableProduct salableProduct,String code,String price){
		salableProduct.setProduct(productDao.read(code));
		salableProduct.setPrice(commonUtils.getBigDecimal(price));
	}
	
	public void set(Sale sale,String cost,String tax,String turnover){
		/*
		sale.setAccountingPeriod(accountingPeriodDao.select().one());
		sale.setDate(new Date());
		sale.setCashier(cashierDao.select().one());
		sale.getCost().setValue(commonUtils.getBigDecimal(cost));
		sale.getCost().setTax(commonUtils.getBigDecimal(tax));
		sale.getCost().setTurnover(commonUtils.getBigDecimal(turnover));
		*/
	}
}
