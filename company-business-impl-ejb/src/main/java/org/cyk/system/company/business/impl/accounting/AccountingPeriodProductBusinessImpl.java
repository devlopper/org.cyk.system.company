package org.cyk.system.company.business.impl.accounting;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.accounting.AccountingPeriodProductBusiness;
import org.cyk.system.company.business.impl.sale.SaleBusinessImpl;
import org.cyk.system.company.model.accounting.AccountingPeriodProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodProductDao;
import org.cyk.system.root.business.api.Crud;

public class AccountingPeriodProductBusinessImpl extends AbstractAccountingPeriodResultsBusinessImpl<AccountingPeriodProduct, AccountingPeriodProductDao,Product> implements AccountingPeriodProductBusiness, Serializable {

	private static final long serialVersionUID = -1843616492544404846L;
	
	@Inject
	public AccountingPeriodProductBusinessImpl(AccountingPeriodProductDao dao) {
		super(dao);
	}

	@Override
	public void consume(Sale sale, Crud crud, Boolean first) {
		/*Collection<SaleProduct> saleProducts = saleProductDao.readBySale(sale);
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
			AccountingPeriodProduct accountingPeriodProduct = dao.readByAccountingPeriodByEntity(sale.getAccountingPeriod(), salableProduct.getProduct());
			updateSalesResults(accountingPeriodProduct.getSaleResults(),crud,first,usedCount,cost,vat,turnover);
			dao.update(accountingPeriodProduct);
			
			//Update Hierarchy
		 	ProductCategory category = salableProduct.getProduct().getCategory();
			while(category!=null){
				AccountingPeriodProductCategory accountingPeriodProductCategory = accountingPeriodProductCategoryDao.readByAccountingPeriodByEntity(sale.getAccountingPeriod(), category);
				updateSalesResults(accountingPeriodProductCategory.getSaleResults(),crud,first, usedCount,cost,vat, turnover);
				accountingPeriodProductCategoryDao.update(accountingPeriodProductCategory);
				category = productCategoryDao.readParent(category);
			}
		}*/
	}
	/*
	private void updateSalesResults(SaleResults salesResults, Crud crud, Boolean first,BigDecimal count,BigDecimal cost,BigDecimal vat,BigDecimal turnover){
		BigDecimal sign = null;
		if(Crud.CREATE.equals(crud)){
			sign = BigDecimal.ONE;
		}else if(Crud.UPDATE.equals(crud)) {
			sign = BigDecimal.ONE;
		}else if(Crud.DELETE.equals(crud)) {
			sign = BigDecimal.ONE.negate();
		}
		commonUtils.increment(BigDecimal.class, salesResults.getCost(), Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS, count.multiply(sign));
		commonUtils.increment(BigDecimal.class, salesResults.getCost(), Cost.FIELD_VALUE, cost.multiply(sign));
		commonUtils.increment(BigDecimal.class, salesResults.getCost(), Cost.FIELD_TAX, vat.multiply(sign));
		commonUtils.increment(BigDecimal.class, salesResults.getCost(), Cost.FIELD_TURNOVER, turnover.multiply(sign));
	}*/
	
	/**/
	
	public static class SaleBusinessAdapter extends SaleBusinessImpl.Listener.Adapter implements Serializable {
		private static final long serialVersionUID = 5585791722273454192L;
		
		/*@Override
		public void processOnConsume(Sale sale, Crud crud, Boolean first) {
			inject(AccountingPeriodProductBusiness.class).consume(sale,crud,first);
		}*/
	}
	
}
