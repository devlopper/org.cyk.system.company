package org.cyk.system.company.business.impl.accounting;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.cyk.system.company.business.api.accounting.AccountingPeriodProductBusiness;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.accounting.AccountingPeriodProduct;
import org.cyk.system.company.model.accounting.AccountingPeriodProductCategory;
import org.cyk.system.company.model.accounting.SalesResults;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.product.SaleProduct;
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
		Set<Product> products = new HashSet<>();
		for(SaleProduct saleProduct : saleProducts)
			products.add(saleProduct.getProduct());
		
		for(Product product : products){
			BigDecimal usedCount = BigDecimal.ZERO,turnover = BigDecimal.ZERO;
			for(SaleProduct saleProduct : saleProducts)
				if(saleProduct.getProduct().equals(product)){
					usedCount = usedCount.add(saleProduct.getQuantity());
					turnover = turnover.add(saleProduct.getTurnover());
				}
			AccountingPeriodProduct accountingPeriodProduct = dao.readByAccountingPeriodByProduct(accountingPeriod, product);
			updateSalesResults(accountingPeriodProduct.getSalesResults(),usedCount,turnover);
			dao.update(accountingPeriodProduct);
			
			//Update Hierarchy
		 	ProductCategory category = product.getCategory();
			while(category!=null){
				AccountingPeriodProductCategory accountingPeriodProductCategory = accountingPeriodProductCategoryDao.readByAccountingPeriodByProduct(accountingPeriod, category);
				updateSalesResults(accountingPeriodProductCategory.getSalesResults(), usedCount, turnover);
				accountingPeriodProductCategoryDao.update(accountingPeriodProductCategory);
				category = productCategoryDao.readParent(category);
			}
		}
	}
	
	private void updateSalesResults(SalesResults salesResults,BigDecimal count,BigDecimal turnover){
		salesResults.setCount(salesResults.getCount().add(count));
		salesResults.setTurnover(salesResults.getTurnover().add(turnover));
	}
	
}
