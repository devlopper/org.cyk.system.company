package org.cyk.system.company.ui.web.primefaces.adapter.sale;

import java.io.Serializable;

import javax.servlet.ServletContextEvent;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.company.ui.web.primefaces.AbstractCompanyContextListener;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.company.ui.web.primefaces.sale.SalableProductEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SalableProductInstanceCashRegisterEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SalableProductInstanceEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SaleEditPage;
import org.cyk.system.root.business.impl.language.LanguageBusinessImpl;
import org.cyk.system.root.ui.web.primefaces.api.RootWebManager;

public abstract class AbstractSaleOnlyOneInstanceContextListener extends AbstractCompanyContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;

	@SuppressWarnings({ })
	@Override
	public void contextInitialized(ServletContextEvent event) {
		SaleEditPage.FORM_EDIT_CLASS = SaleEditPage.FormOneSaleProduct.class;
		SaleEditPage.SHOW_QUANTITY_COLUMN = Boolean.FALSE;
		SaleEditPage.SHOW_UNIT_PRICE_COLUMN = Boolean.FALSE;
		SalableProductEditPage.CREATE_ON_PRODUCT = Boolean.FALSE;
		SalableProductInstanceEditPage.CREATE_ON_SALABLE_PRODUCT = Boolean.FALSE;
		SalableProductInstanceCashRegisterEditPage.CREATE_ON_CASH_REGISTER=Boolean.FALSE;
		
		LanguageBusinessImpl.cache(Product.class, getOneProductName(),getManyProductName());
		LanguageBusinessImpl.cache(SalableProduct.class, getOneProductName(),getManyProductName());
		LanguageBusinessImpl.cache(SalableProductInstance.class, getOneInstanceName(),getManyInstanceName());
		LanguageBusinessImpl.cache(SalableProductInstanceCashRegister.class, getOneInstanceCashRegisterName(),getManyInstanceCashRegisterName());
		
		super.contextInitialized(event);
	}
		
	@Override
	protected void identifiableConfiguration(ServletContextEvent event) {
		super.identifiableConfiguration(event);
		uiManager.registerApplicationUImanager(RootWebManager.getInstance());
		uiManager.registerApplicationUImanager(CompanyWebManager.getInstance());
	}
	
	protected abstract String getOneProductName();
	protected abstract String getManyProductName();
	protected abstract String getOneInstanceName();
	protected abstract String getManyInstanceName();
	protected abstract String getOneInstanceCashRegisterName();
	protected abstract String getManyInstanceCashRegisterName();
	
}
