package org.cyk.system.company.ui.web.primefaces.adapter.giftcard;

import java.io.Serializable;

import javax.servlet.ServletContextEvent;

import org.cyk.system.company.business.impl.sale.SaleBusinessImpl;
import org.cyk.system.company.business.impl.sale.SaleProductInstanceBusinessImpl;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.company.ui.web.primefaces.adapter.sale.AbstractSaleOnlyOneInstanceContextListener;
import org.cyk.system.company.ui.web.primefaces.sale.SaleEditPage;
import org.cyk.system.root.ui.web.primefaces.api.RootWebManager;
import org.cyk.ui.web.primefaces.page.AbstractBusinessEntityFormOnePage;
import org.cyk.ui.web.primefaces.page.AbstractProcessManyPage;
import org.cyk.ui.web.primefaces.page.AbstractSelectManyPage;

public abstract class AbstractGiftCardContextListener extends AbstractSaleOnlyOneInstanceContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;

	@SuppressWarnings({ })
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		
		SaleBusinessImpl.Listener.COLLECTION.add(new SaleBusinessAdapter());
		SaleProductInstanceBusinessImpl.Listener.COLLECTION.add(new SaleProductInstanceBusinessAdapter());
		//FiniteStateMachineStateLogBusinessImpl.Listener.COLLECTION.add(new FiniteStateMachineStateLogBusinessAdapter());
		
		CompanyWebManager.getInstance().getListeners().add(new GiftCardPrimefacesManagerAdapter());
		AbstractBusinessEntityFormOnePage.BusinessEntityFormOnePageListener.COLLECTION.add(new BusinessSaleEditPageAdapter());
		SaleEditPage.Listener.COLLECTION.add(new SaleEditPageAdapter());
		
		AbstractSelectManyPage.Listener.COLLECTION.add(new BusinessSelectManySalableProductInstancePageAdapter());
		AbstractProcessManyPage.Listener.COLLECTION.add(new BusinessProcessManySalableProductInstancePageAdapter());
	}
		
	@Override
	protected void identifiableConfiguration(ServletContextEvent event) {
		super.identifiableConfiguration(event);
		uiManager.registerApplicationUImanager(RootWebManager.getInstance());
		uiManager.registerApplicationUImanager(CompanyWebManager.getInstance());
	}

	@Override
	protected String getOneProductName() {
		return "carte cadeau";
	}

	@Override
	protected String getManyProductName() {
		return "cartes cadeau";
	}

	@Override
	protected String getOneInstanceName() {
		return "unité de carte cadeau";
	}

	@Override
	protected String getManyInstanceName() {
		return "unités de carte cadeau";
	}

	@Override
	protected String getOneInstanceCashRegisterName() {
		return "association d'unité de carte cadeau et vendeur";
	}

	@Override
	protected String getManyInstanceCashRegisterName() {
		return "associations d'unité de carte cadeau et vendeur";
	}
	
	
	
}
