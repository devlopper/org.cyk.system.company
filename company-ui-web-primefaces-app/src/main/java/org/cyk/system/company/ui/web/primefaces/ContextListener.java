package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.ui.web.primefaces.production.ResellerCrudOnePageAdapter;
import org.cyk.system.root.model.party.person.AbstractActor;
import org.cyk.system.root.ui.web.primefaces.api.RootWebManager;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.primefaces.page.party.AbstractActorCrudOnePageAdapter;

@WebListener
public class ContextListener extends AbstractCompanyContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;

	//@Inject private CompanyRandomDataProvider companyRandomDataProvider;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		//SalableProductEditPage.CREATE_ON_PRODUCT = Boolean.FALSE;
		//SalableProductInstanceEditPage.CREATE_ON_SALABLE_PRODUCT = Boolean.FALSE;
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
