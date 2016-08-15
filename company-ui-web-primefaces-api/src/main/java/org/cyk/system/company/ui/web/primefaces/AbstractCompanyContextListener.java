package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;

import javax.servlet.ServletContextEvent;

import org.cyk.system.company.business.impl.payment.CashRegisterDetails;
import org.cyk.system.company.business.impl.payment.CashRegisterMovementDetails;
import org.cyk.system.company.business.impl.payment.CashRegisterMovementModeDetails;
import org.cyk.system.company.business.impl.payment.CashRegisterMovementTermCollectionDetails;
import org.cyk.system.company.business.impl.payment.CashRegisterMovementTermDetails;
import org.cyk.system.company.business.impl.payment.CashierDetails;
import org.cyk.system.company.business.impl.product.IntangibleProductDetails;
import org.cyk.system.company.business.impl.product.ProductCollectionDetails;
import org.cyk.system.company.business.impl.product.TangibleProductDetails;
import org.cyk.system.company.business.impl.sale.CustomerSalableProductDetails;
import org.cyk.system.company.business.impl.sale.SalableProductDetails;
import org.cyk.system.company.business.impl.sale.SalableProductInstanceCashRegisterDetails;
import org.cyk.system.company.business.impl.sale.SalableProductInstanceDetails;
import org.cyk.system.company.business.impl.sale.SaleCashRegisterMovementDetails;
import org.cyk.system.company.business.impl.sale.SaleDetails;
import org.cyk.system.company.business.impl.structure.CompanyDetails;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.payment.CashRegisterMovementMode;
import org.cyk.system.company.model.payment.CashRegisterMovementTerm;
import org.cyk.system.company.model.payment.CashRegisterMovementTermCollection;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.ProductCollection;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.CustomerSalableProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleProductInstance;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.ui.web.primefaces.payment.CashRegisterEditPage;
import org.cyk.system.company.ui.web.primefaces.payment.CashRegisterMovementEditPage;
import org.cyk.system.company.ui.web.primefaces.payment.CashRegisterMovementModeEditPage;
import org.cyk.system.company.ui.web.primefaces.payment.CashRegisterMovementTermCollectionEditPage;
import org.cyk.system.company.ui.web.primefaces.payment.CashRegisterMovementTermEditPage;
import org.cyk.system.company.ui.web.primefaces.payment.CashierEditPage;
import org.cyk.system.company.ui.web.primefaces.product.IntangibleProductEditPage;
import org.cyk.system.company.ui.web.primefaces.product.ProductCollectionEditPage;
import org.cyk.system.company.ui.web.primefaces.product.TangibleProductEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.CustomerSalableProductEditPage;
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
import org.cyk.ui.web.primefaces.page.AbstractProcessManyPage;
import org.cyk.ui.web.primefaces.page.AbstractSelectManyPage;
import org.cyk.ui.web.primefaces.page.AbstractSelectOnePage;

public abstract class AbstractCompanyContextListener extends AbstractContextListener implements Serializable {

	private static final long serialVersionUID = -6496963238759008827L;

	@Override
	protected void identifiableConfiguration(ServletContextEvent event) {
		super.identifiableConfiguration(event);
		uiManager.registerApplicationUImanager(CompanyWebManager.getInstance());
		
		initializeProductModule();
		
		initializePaymentModule();
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(Company.class, CompanyEditPage.Form.class, CompanyDetails.class,null,null,null));
		uiManager.configBusinessIdentifiable(Company.class, null);
		/*
		uiManager.registerConfiguration(new IdentifiableConfiguration(CashRegister.class, CashRegisterEditPage.Form.class, CashRegisterDetails.class,null,null,null));
		uiManager.configBusinessIdentifiable(CashRegister.class, null);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(CashRegisterMovement.class, CashRegisterMovementEditPage.Form.class, CashRegisterMovementDetails.class,null,null,null));
		uiManager.configBusinessIdentifiable(CashRegisterMovement.class, null);
		*/
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
		AbstractSelectManyPage.Listener.COLLECTION.add(new SalableProductInstanceCashRegisterQueryManyFormModel.PageAdapter());
		AbstractProcessManyPage.Listener.COLLECTION.add(new SalableProductInstanceCashRegisterQueryManyFormModel.ProcessPageAdapter());
		webNavigationManager.useDynamicSelectView(SalableProductInstanceCashRegister.class);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(Sale.class, SaleEditPage.FORM_EDIT_CLASS, SaleDetails.class,SaleQueryFormModel.class,null,null));
		uiManager.configBusinessIdentifiable(Sale.class, null);
		AbstractSelectOnePage.Listener.COLLECTION.add(new SaleQueryFormModel.PageAdapter());
		webNavigationManager.useDynamicSelectView(Sale.class);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(SaleProductInstance.class, null, null,SaleProductInstanceQueryOneFormModel.class,null,null));
		uiManager.configBusinessIdentifiable(SaleProductInstance.class, null);
		AbstractSelectOnePage.Listener.COLLECTION.add(new SaleProductInstanceQueryOneFormModel.PageAdapter());
		webNavigationManager.useDynamicSelectView(SaleProductInstance.class);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(SaleCashRegisterMovement.class, SaleCashRegisterMovementEditPage.Form.class, SaleCashRegisterMovementDetails.class,null,null,null));
		uiManager.configBusinessIdentifiable(SaleCashRegisterMovement.class, null);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(CustomerSalableProduct.class, CustomerSalableProductEditPage.Form.class, CustomerSalableProductDetails.class,null,null,null));
		uiManager.configBusinessIdentifiable(CustomerSalableProduct.class, null);
		//webNavigationManager.useDynamicSelectView(SalableProductInstance.class);
	}
	
	protected void initializeProductModule(){
		uiManager.registerConfiguration(new IdentifiableConfiguration(TangibleProduct.class, TangibleProductEditPage.Form.class, TangibleProductDetails.class,null,null,null));
		uiManager.configBusinessIdentifiable(TangibleProduct.class, null);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(IntangibleProduct.class, IntangibleProductEditPage.Form.class, IntangibleProductDetails.class,null,null,null));
		uiManager.configBusinessIdentifiable(IntangibleProduct.class, null);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(ProductCollection.class, ProductCollectionEditPage.Form.class, ProductCollectionDetails.class,null,null,null));
		uiManager.configBusinessIdentifiable(ProductCollection.class, null);
		
	}
	
	protected void initializePaymentModule(){
		uiManager.registerConfiguration(new IdentifiableConfiguration(Cashier.class, CashierEditPage.Form.class, CashierDetails.class,null,null,null));
		uiManager.configBusinessIdentifiable(Cashier.class, null);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(CashRegister.class, CashRegisterEditPage.Form.class, CashRegisterDetails.class,null,null,null));
		uiManager.configBusinessIdentifiable(CashRegister.class, null);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(CashRegisterMovement.class, CashRegisterMovementEditPage.Form.class, CashRegisterMovementDetails.class,null,null,null));
		uiManager.configBusinessIdentifiable(CashRegisterMovement.class, null);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(CashRegisterMovementMode.class, CashRegisterMovementModeEditPage.Form.class, CashRegisterMovementModeDetails.class,null,null,null));
		uiManager.configBusinessIdentifiable(CashRegisterMovementMode.class, null);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(CashRegisterMovementTerm.class, CashRegisterMovementTermEditPage.Form.class, CashRegisterMovementTermDetails.class,null,null,null));
		uiManager.configBusinessIdentifiable(CashRegisterMovementTerm.class, null);
		
		uiManager.registerConfiguration(new IdentifiableConfiguration(CashRegisterMovementTermCollection.class, CashRegisterMovementTermCollectionEditPage.Form.class, CashRegisterMovementTermCollectionDetails.class,null,null,null));
		uiManager.configBusinessIdentifiable(CashRegisterMovementTermCollection.class, null);
		
	}
}
