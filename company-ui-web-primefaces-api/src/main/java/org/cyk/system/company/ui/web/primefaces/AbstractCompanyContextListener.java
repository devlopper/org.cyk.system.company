package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;

import javax.servlet.ServletContextEvent;

import org.cyk.system.company.business.impl.payment.CashRegisterDetails;
import org.cyk.system.company.business.impl.payment.CashRegisterMovementDetails;
import org.cyk.system.company.business.impl.sale.SalableProductDetails;
import org.cyk.system.company.business.impl.sale.SalableProductInstanceCashRegisterDetails;
import org.cyk.system.company.business.impl.sale.SalableProductInstanceDetails;
import org.cyk.system.company.business.impl.sale.SaleCashRegisterMovementDetails;
import org.cyk.system.company.business.impl.sale.SaleDetails;
import org.cyk.system.company.business.impl.structure.CompanyDetails;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleProductInstance;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.ui.web.primefaces.payment.CashRegisterEditPage;
import org.cyk.system.company.ui.web.primefaces.payment.CashRegisterMovementEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SalableProductEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SalableProductInstanceCashRegisterEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SalableProductInstanceCashRegisterQueryManyFormModel;
import org.cyk.system.company.ui.web.primefaces.sale.SalableProductInstanceEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SaleCashRegisterMovementEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SaleEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SaleProductInstanceQueryOneFormModel;
import org.cyk.system.company.ui.web.primefaces.sale.SaleQueryFormModel;
import org.cyk.system.company.ui.web.primefaces.structure.CompanyEditPage;
import org.cyk.ui.api.config.IdentifiableConfiguration;
import org.cyk.ui.web.primefaces.AbstractContextListener;

public abstract class AbstractCompanyContextListener extends AbstractContextListener implements Serializable {

	private static final long serialVersionUID = -6496963238759008827L;

	@Override
	protected void identifiableConfiguration(ServletContextEvent event) {
		super.identifiableConfiguration(event);
		uiManager.registerConfiguration(new IdentifiableConfiguration(Company.class, CompanyEditPage.Form.class, CompanyDetails.class,null,null,null));
		uiManager.configBusinessIdentifiable(Company.class, null);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(CashRegister.class, CashRegisterEditPage.Form.class, CashRegisterDetails.class,null,null,null));
		uiManager.configBusinessIdentifiable(CashRegister.class, null);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(CashRegisterMovement.class, CashRegisterMovementEditPage.Form.class, CashRegisterMovementDetails.class,null,null,null));
		uiManager.configBusinessIdentifiable(CashRegisterMovement.class, null);
		
		/* Sale */
		uiManager.registerConfiguration(new IdentifiableConfiguration(SalableProduct.class, SalableProductEditPage.Form.class, SalableProductDetails.class,null,null,null));
		uiManager.configBusinessIdentifiable(SalableProduct.class, null);
		//webNavigationManager.useDynamicSelectView(SalableProduct.class);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(SalableProductInstance.class, SalableProductInstanceEditPage.Form.class, SalableProductInstanceDetails.class,null,null,null));
		uiManager.configBusinessIdentifiable(SalableProductInstance.class, null);
		//webNavigationManager.useDynamicSelectView(SalableProductInstance.class);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(SalableProductInstanceCashRegister.class, SalableProductInstanceCashRegisterEditPage.Form.class
				,SalableProductInstanceCashRegisterDetails.class,null,null,SalableProductInstanceCashRegisterQueryManyFormModel.class));
		uiManager.configBusinessIdentifiable(SalableProductInstanceCashRegister.class, null);
		primefacesManager.getSelectManyPageListeners().add(new SalableProductInstanceCashRegisterQueryManyFormModel.PageAdapter());
		primefacesManager.getProcessManyPageListeners().add(new SalableProductInstanceCashRegisterQueryManyFormModel.ProcessPageAdapter());
		webNavigationManager.useDynamicSelectView(SalableProductInstanceCashRegister.class);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(Sale.class, SaleEditPage.FORM_EDIT_CLASS, SaleDetails.class,SaleQueryFormModel.class,null,null));
		uiManager.configBusinessIdentifiable(Sale.class, null);
		primefacesManager.getSelectOnePageListeners().add(new SaleQueryFormModel.PageAdapter());
		webNavigationManager.useDynamicSelectView(Sale.class);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(SaleProductInstance.class, null, null,SaleProductInstanceQueryOneFormModel.class,null,null));
		uiManager.configBusinessIdentifiable(SaleProductInstance.class, null);
		primefacesManager.getSelectOnePageListeners().add(new SaleProductInstanceQueryOneFormModel.PageAdapter());
		webNavigationManager.useDynamicSelectView(SaleProductInstance.class);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(SaleCashRegisterMovement.class, SaleCashRegisterMovementEditPage.Form.class, SaleCashRegisterMovementDetails.class,null,null,null));
		uiManager.configBusinessIdentifiable(SaleCashRegisterMovement.class, null);
		
	}
	
}
