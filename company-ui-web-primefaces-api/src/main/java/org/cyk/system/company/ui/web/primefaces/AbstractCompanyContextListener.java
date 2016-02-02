package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;

import javax.servlet.ServletContextEvent;

import org.cyk.system.company.business.impl.payment.CashRegisterDetails;
import org.cyk.system.company.business.impl.payment.CashRegisterMovementDetails;
import org.cyk.system.company.business.impl.sale.SaleCashRegisterMovementDetails;
import org.cyk.system.company.business.impl.sale.SaleDetails;
import org.cyk.system.company.business.impl.structure.CompanyDetails;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.ui.web.primefaces.payment.CashRegisterEditPage;
import org.cyk.system.company.ui.web.primefaces.payment.CashRegisterMovementEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SaleCashRegisterMovementEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SaleEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SaleQueryFormModel;
import org.cyk.system.company.ui.web.primefaces.structure.CompanyEditPage;
import org.cyk.ui.api.config.IdentifiableConfiguration;
import org.cyk.ui.web.primefaces.AbstractContextListener;

public abstract class AbstractCompanyContextListener extends AbstractContextListener implements Serializable {

	private static final long serialVersionUID = -6496963238759008827L;

	@Override
	protected void identifiableConfiguration(ServletContextEvent event) {
		super.identifiableConfiguration(event);
		uiManager.registerConfiguration(new IdentifiableConfiguration(Company.class, CompanyEditPage.Form.class, CompanyDetails.class,null));
		uiManager.configBusinessIdentifiable(Company.class, null);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(CashRegister.class, CashRegisterEditPage.Form.class, CashRegisterDetails.class,null));
		uiManager.configBusinessIdentifiable(CashRegister.class, null);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(CashRegisterMovement.class, CashRegisterMovementEditPage.Form.class, CashRegisterMovementDetails.class,null));
		uiManager.configBusinessIdentifiable(CashRegisterMovement.class, null);
		
		/* Sale */
		uiManager.registerConfiguration(new IdentifiableConfiguration(Sale.class, SaleEditPage.Form.class, SaleDetails.class,SaleQueryFormModel.class));
		uiManager.configBusinessIdentifiable(Sale.class, null);
		primefacesManager.getSelectPageListeners().add(new SaleQueryFormModel.PageAdapter());
		webNavigationManager.useDynamicSelectView(Sale.class);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(SaleCashRegisterMovement.class, SaleCashRegisterMovementEditPage.Form.class, SaleCashRegisterMovementDetails.class,null));
		uiManager.configBusinessIdentifiable(SaleCashRegisterMovement.class, null);
		
	}
	
}
