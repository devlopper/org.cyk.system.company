package org.cyk.system.company.business.impl.accounting;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.cyk.system.company.business.api.accounting.AccountingPeriodProductBusiness;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.accounting.AccountingPeriodProduct;
import org.cyk.system.company.model.accounting.AccountingPeriodProductCategory;
import org.cyk.system.company.model.accounting.SaleResults;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodProductCategoryDao;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodProductDao;
import org.cyk.system.company.persistence.api.product.ProductCategoryDao;

public class AccountingPeriodProductBusinessImpl extends AbstractAccountingPeriodResultsBusinessImpl<AccountingPeriodProduct, AccountingPeriodProductDao,Product> implements AccountingPeriodProductBusiness, Serializable {

	private static final long serialVersionUID = -1843616492544404846L;
	
	@Inject private ProductCategoryDao productCategoryDao;
	@Inject private AccountingPeriodProductCategoryDao accountingPeriodProductCategoryDao;
	
	@Inject
	public AccountingPeriodProductBusinessImpl(AccountingPeriodProductDao dao) {
		super(dao);
	}

	@Override
	public void consume(AccountingPeriod accountingPeriod,Collection<SaleProduct> saleProducts) {
		Set<SalableProduct> products = new HashSet<>();
		for(SaleProduct saleProduct : saleProducts)
			products.add(saleProduct.getSalableProduct());
		
		for(SalableProduct salableProduct : products){
			BigDecimal usedCount = BigDecimal.ZERO,cost = BigDecimal.ZERO,vat = BigDecimal.ZERO,turnover = BigDecimal.ZERO;
			for(SaleProduct saleProduct : saleProducts)
				if(saleProduct.getSalableProduct().equals(salableProduct)){
					usedCount = usedCount.add(saleProduct.getQuantity());
					cost = turnover.add(saleProduct.getCost().getValue());
					vat = turnover.add(saleProduct.getCost().getTax());
					turnover = turnover.add(saleProduct.getCost().getTurnover());
				}
			AccountingPeriodProduct accountingPeriodProduct = dao.readByAccountingPeriodByProduct(accountingPeriod, salableProduct.getProduct());
			updateSalesResults(accountingPeriodProduct.getSaleResults(),usedCount,cost,vat,turnover);
			dao.update(accountingPeriodProduct);
			
			//Update Hierarchy
		 	ProductCategory category = salableProduct.getProduct().getCategory();
			while(category!=null){
				AccountingPeriodProductCategory accountingPeriodProductCategory = accountingPeriodProductCategoryDao.readByAccountingPeriodByProduct(accountingPeriod, category);
				updateSalesResults(accountingPeriodProductCategory.getSaleResults(), usedCount,cost,vat, turnover);
				accountingPeriodProductCategoryDao.update(accountingPeriodProductCategory);
				category = productCategoryDao.readParent(category);
			}
		}
	}
	
	private void updateSalesResults(SaleResults salesResults,BigDecimal count,BigDecimal cost,BigDecimal vat,BigDecimal turnover){
		commonUtils.increment(BigDecimal.class, salesResults.getCost(), Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS, count);
		commonUtils.increment(BigDecimal.class, salesResults.getCost(), Cost.FIELD_VALUE, cost);
		commonUtils.increment(BigDecimal.class, salesResults.getCost(), Cost.FIELD_TAX, vat);
		commonUtils.increment(BigDecimal.class, salesResults.getCost(), Cost.FIELD_TURNOVER, turnover);
	}
	
}
