package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.SaleProductBusiness;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.company.persistence.api.product.SaleProductDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

public class SaleProductBusinessImpl extends AbstractTypedBusinessService<SaleProduct, SaleProductDao> implements SaleProductBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public SaleProductBusinessImpl(SaleProductDao dao) {
		super(dao);
	}

	@Override
	public void process(SaleProduct saleProduct) {
		saleProduct.setPrice(saleProduct.getProduct().getPrice().multiply(saleProduct.getQuantity()).subtract(saleProduct.getReduction()));
		AccountingPeriod accountingPeriod = saleProduct.getSale().getAccountingPeriod();
		if(Boolean.TRUE.equals(accountingPeriod.getValueAddedTaxIncludedInCost())){
			BigDecimal divider = BigDecimal.ONE.add(accountingPeriod.getValueAddedTaxRate());
			saleProduct.setValueAddedTax(saleProduct.getPrice().divide(divider).subtract(saleProduct.getPrice()));
			saleProduct.setTurnover(saleProduct.getPrice().subtract(saleProduct.getValueAddedTax()));
		}else{
			saleProduct.setValueAddedTax(accountingPeriod.getValueAddedTaxRate().multiply(saleProduct.getPrice()));
			saleProduct.setTurnover(saleProduct.getPrice());
			//TODO price should be updated????
		}
		
	}
	
}
