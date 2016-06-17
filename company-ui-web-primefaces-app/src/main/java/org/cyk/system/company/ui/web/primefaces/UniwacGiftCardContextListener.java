package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;
import java.util.Locale;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.ui.web.primefaces.production.ResellerCrudOnePageAdapter;
import org.cyk.system.company.ui.web.primefaces.sale.SalableProductEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SalableProductInstanceEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SaleEditPage;
import org.cyk.system.root.business.impl.language.LanguageBusinessImpl;
import org.cyk.system.root.model.party.person.AbstractActor;
import org.cyk.system.root.ui.web.primefaces.api.RootWebManager;
import org.cyk.ui.api.command.menu.SystemMenu;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.primefaces.AbstractPrimefacesManager;
import org.cyk.ui.web.primefaces.UserSession;
import org.cyk.ui.web.primefaces.page.tools.AbstractActorCrudOnePageAdapter;
import org.cyk.utility.common.helper.StringHelper.CaseType;

@WebListener
public class UniwacGiftCardContextListener extends AbstractCompanyContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		SaleEditPage.SHOW_QUANTITY_COLUMN = Boolean.FALSE;
		
		LanguageBusinessImpl.cache(Locale.FRENCH, "model.entity.salableProduct", null, CaseType.FURL, "carte cadeau");
		LanguageBusinessImpl.cache(Locale.FRENCH, "model.entity.salableProduct.many", null, CaseType.FURL, "cartes cadeau");
		
		LanguageBusinessImpl.cache(Locale.FRENCH, "model.entity.salableProductInstance", null, CaseType.FURL, "unité de carte cadeau");
		LanguageBusinessImpl.cache(Locale.FRENCH, "model.entity.salableProductInstance.many", null, CaseType.FURL, "unités de carte cadeau");
		
		LanguageBusinessImpl.cache(Locale.FRENCH, "model.entity.salableProductInstanceCashRegister", null, CaseType.FURL, "association d'unité de carte cadeau et vendeur");
		LanguageBusinessImpl.cache(Locale.FRENCH, "model.entity.salableProductInstanceCashRegister.many", null, CaseType.FURL, "associations d'unité de carte cadeau et vendeur");
		
		super.contextInitialized(event);
		SalableProductEditPage.CREATE_ON_PRODUCT = Boolean.FALSE;
		SalableProductInstanceEditPage.CREATE_ON_SALABLE_PRODUCT = Boolean.FALSE;
		
		CompanyWebManager.getInstance().getListeners().add(new AbstractPrimefacesManager.AbstractPrimefacesManagerListener.Adapter(){
			private static final long serialVersionUID = 1L;
			@Override
			public SystemMenu getSystemMenu(UserSession userSession) {
				return GiftCardSystemMenuBuilder.getInstance().build(userSession);
			}
		});
	}
	
	@Override
	protected <ACTOR extends AbstractActor> void registerActorForm(Class<ACTOR> actorClass) {
		super.registerActorForm(actorClass);
		if(Reseller.class.equals(actorClass)){
			;//registerBusinessEntityFormOnePageListener(Reseller.class,new ResellerCrudOnePageAdapter());
		}else
			;
	}
	
	@Override
	protected Class<? extends AbstractFormModel<?>> getEditFormModelClass(Class<?> clazz) {
		if(Reseller.class.equals(clazz)){
			return ResellerCrudOnePageAdapter.ResellerEditFormModel.class;
		}else
			return super.getEditFormModelClass(clazz);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected <ACTOR extends AbstractActor> AbstractActorCrudOnePageAdapter<ACTOR> getActorCrudOnePageAdapter(Class<ACTOR> actorClass) {
		if(Reseller.class.equals(actorClass)){
			return (AbstractActorCrudOnePageAdapter<ACTOR>) new ResellerCrudOnePageAdapter();
		}else
			return super.getActorCrudOnePageAdapter(actorClass);
	}
		
	@Override
	protected void identifiableConfiguration(ServletContextEvent event) {
		super.identifiableConfiguration(event);
		uiManager.registerApplicationUImanager(RootWebManager.getInstance());
		uiManager.registerApplicationUImanager(CompanyWebManager.getInstance());
	}
	
	
	
}
