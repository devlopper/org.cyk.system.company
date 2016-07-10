package org.cyk.system.company.ui.web.primefaces.uniwaxgiftcard;

import java.io.Serializable;
import java.util.Locale;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.cyk.system.company.business.impl.sale.SaleBusinessImpl;
import org.cyk.system.company.business.impl.sale.SaleProductInstanceBusinessImpl;
import org.cyk.system.company.ui.web.primefaces.AbstractCompanyContextListener;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.company.ui.web.primefaces.adapter.giftcard.BusinessProcessManySalableProductInstancePageAdapter;
import org.cyk.system.company.ui.web.primefaces.adapter.giftcard.BusinessSaleEditPageAdapter;
import org.cyk.system.company.ui.web.primefaces.adapter.giftcard.BusinessSelectManySalableProductInstancePageAdapter;
import org.cyk.system.company.ui.web.primefaces.adapter.giftcard.FiniteStateMachineStateLogBusinessAdapter;
import org.cyk.system.company.ui.web.primefaces.adapter.giftcard.GiftCardPrimefacesManagerAdapter;
import org.cyk.system.company.ui.web.primefaces.adapter.giftcard.SaleBusinessAdapter;
import org.cyk.system.company.ui.web.primefaces.adapter.giftcard.SaleEditPageAdapter;
import org.cyk.system.company.ui.web.primefaces.adapter.giftcard.SaleProductInstanceBusinessAdapter;
import org.cyk.system.company.ui.web.primefaces.sale.SalableProductEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SalableProductInstanceCashRegisterEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SalableProductInstanceEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SaleEditPage;
import org.cyk.system.root.business.impl.language.LanguageBusinessImpl;
import org.cyk.system.root.business.impl.mathematics.machine.FiniteStateMachineStateLogBusinessImpl;
import org.cyk.system.root.ui.web.primefaces.api.RootWebManager;
import org.cyk.ui.web.primefaces.page.AbstractBusinessEntityFormOnePage;
import org.cyk.ui.web.primefaces.page.AbstractProcessManyPage;
import org.cyk.ui.web.primefaces.page.AbstractSelectManyPage;
import org.cyk.utility.common.helper.StringHelper.CaseType;

@WebListener
public class UniwacGiftCardContextListener extends AbstractCompanyContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;

	@SuppressWarnings({ })
	@Override
	public void contextInitialized(ServletContextEvent event) {
		SaleEditPage.FORM_EDIT_CLASS = SaleEditPage.FormOneSaleProduct.class;
		SaleEditPage.SHOW_QUANTITY_COLUMN = Boolean.FALSE;
		SaleEditPage.SHOW_UNIT_PRICE_COLUMN = Boolean.FALSE;
		SalableProductInstanceCashRegisterEditPage.CREATE_ON_CASH_REGISTER=Boolean.FALSE;
		
		LanguageBusinessImpl.cache(Locale.FRENCH, "model.entity.product", null, CaseType.FURL, "carte cadeau");
		LanguageBusinessImpl.cache(Locale.FRENCH, "model.entity.product.many", null, CaseType.FURL, "cartes cadeau");
		
		LanguageBusinessImpl.cache(Locale.FRENCH, "model.entity.salableProduct", null, CaseType.FURL, "carte cadeau");
		LanguageBusinessImpl.cache(Locale.FRENCH, "model.entity.salableProduct.many", null, CaseType.FURL, "cartes cadeau");
		
		LanguageBusinessImpl.cache(Locale.FRENCH, "model.entity.salableProductInstance", null, CaseType.FURL, "unité de carte cadeau");
		LanguageBusinessImpl.cache(Locale.FRENCH, "model.entity.salableProductInstance.many", null, CaseType.FURL, "unités de carte cadeau");
		
		LanguageBusinessImpl.cache(Locale.FRENCH, "model.entity.salableProductInstanceCashRegister", null, CaseType.FURL, "association d'unité de carte cadeau et vendeur");
		LanguageBusinessImpl.cache(Locale.FRENCH, "model.entity.salableProductInstanceCashRegister.many", null, CaseType.FURL, "associations d'unité de carte cadeau et vendeur");
		
		super.contextInitialized(event);
		SalableProductEditPage.CREATE_ON_PRODUCT = Boolean.FALSE;
		SalableProductInstanceEditPage.CREATE_ON_SALABLE_PRODUCT = Boolean.FALSE;
		
		SaleBusinessImpl.Listener.COLLECTION.add(new SaleBusinessAdapter());
		SaleProductInstanceBusinessImpl.Listener.COLLECTION.add(new SaleProductInstanceBusinessAdapter());
		FiniteStateMachineStateLogBusinessImpl.Listener.COLLECTION.add(new FiniteStateMachineStateLogBusinessAdapter());
		
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
	
	
	
}
