package org.cyk.system.company.ui.web.primefaces.enterpriseresourceplanning;

import java.io.Serializable;
import java.util.Locale;

import javax.servlet.ServletContextEvent;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.sale.SaleBusinessImpl;
import org.cyk.system.company.business.impl.sale.SaleCashRegisterMovementCollectionBusinessImpl;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.company.ui.web.primefaces.adapter.enterpriseresourceplanning.AbstractContextListener;
import org.cyk.system.company.ui.web.primefaces.adapter.enterpriseresourceplanning.PrimefacesManager;
import org.cyk.system.root.business.api.GenericBusiness;
import org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl;

@javax.servlet.annotation.WebListener
public class ContextListener extends AbstractContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		CompanyWebManager.getInstance().getListeners().add(new PrimefacesManager());
		CompanyBusinessLayer.getInstance().enableEnterpriseResourcePlanning();
		
		AbstractIdentifiableBusinessServiceImpl.addAutoSetPropertyValueClass(new String[]{"code","name"}
			, SalableProductCollectionItem.class);
		
		SaleBusinessImpl.Listener.COLLECTION.add(new SaleBusinessImpl.Listener.Adapter.Default.EnterpriseResourcePlanning(){
			private static final long serialVersionUID = 1L;
			@Override
			public void afterCreate(Sale sale) {
				super.afterCreate(sale);
				inject(GenericBusiness.class).createReportFile(sale, CompanyConstant.Code.ReportTemplate.INVOICE, Locale.ENGLISH);
			}
		});
		
		SaleCashRegisterMovementCollectionBusinessImpl.Listener.COLLECTION.add(new SaleCashRegisterMovementCollectionBusinessImpl.Listener.Adapter.Default.EnterpriseResourcePlanning(){
			private static final long serialVersionUID = 1L;
			@Override
			public void afterCreate(SaleCashRegisterMovementCollection saleCashRegisterMovementCollection) {
				super.afterCreate(saleCashRegisterMovementCollection);
				inject(GenericBusiness.class).createReportFile(saleCashRegisterMovementCollection, CompanyConstant.Code.ReportTemplate.SALE_CASH_REGISTER_MOVEMENT_COLLECTION_A4, Locale.ENGLISH);
			}
		});
	}
	
}
