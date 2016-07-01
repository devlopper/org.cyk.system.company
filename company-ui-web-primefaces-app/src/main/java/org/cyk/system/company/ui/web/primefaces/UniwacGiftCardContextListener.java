package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Locale;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.sale.SaleBusinessImpl;
import org.cyk.system.company.business.impl.sale.SaleProductInstanceBusinessImpl;
import org.cyk.system.company.model.payment.CashRegisterMovementMode;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleProductInstance;
import org.cyk.system.company.ui.web.primefaces.production.ResellerCrudOnePageAdapter;
import org.cyk.system.company.ui.web.primefaces.sale.SalableProductEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SalableProductInstanceEditPage;
import org.cyk.system.company.ui.web.primefaces.sale.SaleEditPage;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.language.LanguageBusinessImpl;
import org.cyk.system.root.business.impl.mathematics.machine.FiniteStateMachineStateLogBusinessImpl;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineStateLog.IdentifiablesSearchCriteria;
import org.cyk.system.root.model.party.person.AbstractActor;
import org.cyk.system.root.ui.web.primefaces.api.RootWebManager;
import org.cyk.ui.api.command.menu.SystemMenu;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.primefaces.AbstractPrimefacesManager;
import org.cyk.ui.web.primefaces.UserSession;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.page.AbstractBusinessEntityFormOnePage;
import org.cyk.ui.web.primefaces.page.tools.AbstractActorCrudOnePageAdapter;
import org.cyk.utility.common.cdi.AbstractBean;
import org.cyk.utility.common.helper.StringHelper.CaseType;

@WebListener
public class UniwacGiftCardContextListener extends AbstractCompanyContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void contextInitialized(ServletContextEvent event) {
		SaleEditPage.FORM_EDIT_CLASS = SaleEditPage.FormOneSaleProduct.class;
		SaleEditPage.SHOW_QUANTITY_COLUMN = Boolean.FALSE;
		SaleEditPage.SHOW_UNIT_PRICE_COLUMN = Boolean.FALSE;
		
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
		
		SaleBusinessImpl.Listener.COLLECTION.add(new SaleBusinessImpl.Listener.Adapter(){
			private static final long serialVersionUID = 6995530308698881653L;
			@Override
			public void beforeCreate(Sale sale) {
				if(GiftCardSystemMenuBuilder.ACTION_SELL_GIFT_CARD.equals(sale.getProcessing().getIdentifier())){
					
				}else if(GiftCardSystemMenuBuilder.ACTION_USE_GIFT_CARD.equals(sale.getProcessing().getIdentifier())){
					//No product selling while using gift card
					//while( !sale.getSaleProducts().isEmpty()  )
					//	CompanyBusinessLayer.getInstance().getSaleBusiness().unselectProduct(sale, sale.getSaleProducts().iterator().next());
					sale.getSaleProducts().clear();
					
					//change state to used
					SalableProductInstance salableProductInstance = CompanyBusinessLayer.getInstance().getSalableProductInstanceDao()
							.read(sale.getSaleCashRegisterMovements().iterator().next().getCashRegisterMovement().getMovement().getSupportingDocumentIdentifier());
					Collection<SalableProductInstanceCashRegister> salableProductInstanceCashRegisters = CompanyBusinessLayer.getInstance().getSalableProductInstanceCashRegisterDao()
							.readBySalableProductInstance(salableProductInstance);
					for(SalableProductInstanceCashRegister salableProductInstanceCashRegister : salableProductInstanceCashRegisters){
						salableProductInstanceCashRegister.setFiniteStateMachineState(rootBusinessLayer.getFiniteStateMachineStateDao().read("Utilisé"));
						salableProductInstanceCashRegister.getFiniteStateMachineState().getProcessing().setParty(sale.getProcessing().getParty());
					}
					CompanyBusinessLayer.getInstance().getSalableProductInstanceCashRegisterBusiness().update(salableProductInstanceCashRegisters);
				}
			}
			@Override
			public Boolean isReportUpdatable(Sale sale) {
				return Boolean.FALSE;
			}
		});
		
		SaleProductInstanceBusinessImpl.Listener.COLLECTION.add(new SaleProductInstanceBusinessImpl.Listener.Adapter(){
			private static final long serialVersionUID = -8980261961629960732L;

			@Override
			public void beforeCreate(SaleProductInstance saleProductInstance) {
				if(GiftCardSystemMenuBuilder.ACTION_SELL_GIFT_CARD.equals(saleProductInstance.getSaleProduct().getSale().getProcessing().getIdentifier())){
					SalableProductInstanceCashRegister salableProductInstanceCashRegister = CompanyBusinessLayer.getInstance().getSalableProductInstanceCashRegisterDao()
							.readBySalableProductInstanceByCashRegister(saleProductInstance.getSalableProductInstance()
									,saleProductInstance.getSaleProduct().getSale().getCashier().getCashRegister());
					
					salableProductInstanceCashRegister.setFiniteStateMachineState(rootBusinessLayer.getFiniteStateMachineStateDao().read("Vendu"));
					salableProductInstanceCashRegister.getFiniteStateMachineState().getProcessing().setParty(saleProductInstance.getProcessing().getParty());
					CompanyBusinessLayer.getInstance().getSalableProductInstanceCashRegisterBusiness().update(salableProductInstanceCashRegister);	
				}else if(GiftCardSystemMenuBuilder.ACTION_USE_GIFT_CARD.equals(saleProductInstance.getSaleProduct().getSale().getProcessing().getIdentifier())){
					
				}	
			}
		});
		
		FiniteStateMachineStateLogBusinessImpl.Listener.COLLECTION.add(new FiniteStateMachineStateLogBusinessImpl.Listener.Adapter(){
			private static final long serialVersionUID = -4637937513750895770L;
			@Override
			public <T extends AbstractIdentifiable> void afterSearchIdentifiablesFind(IdentifiablesSearchCriteria<T> searchCriteria,Collection<T> identifiables) {
				if(SalableProductInstanceCashRegister.class.equals(searchCriteria.getIdentifiableClass())){
					Collection<SalableProductInstanceCashRegister> utilise = new ArrayList<>();
					Collection<String> supportingDocumentIdentifiers = new LinkedHashSet<>();
					FiniteStateMachineState utiliseFiniteStateMachineState = RootBusinessLayer.getInstance().getFiniteStateMachineStateDao().read("Utilisé");
					if(searchCriteria.getFiniteStateMachineStateLog().getFiniteStateMachineStates().contains(utiliseFiniteStateMachineState)){
						for(T identifiable : identifiables){
							if(((SalableProductInstanceCashRegister)identifiable).getFiniteStateMachineState().getCode().equals(utiliseFiniteStateMachineState.getCode()) ){
								utilise.add((SalableProductInstanceCashRegister) identifiable);
								supportingDocumentIdentifiers.add( ((SalableProductInstanceCashRegister)identifiable).getSalableProductInstance().getCode() );
							}
						}
						Collection<SaleCashRegisterMovement> saleCashRegisterMovements = CompanyBusinessLayer.getInstance().getSaleCashRegisterMovementDao()
								.readBySupportingDocumentIdentifiers(supportingDocumentIdentifiers);
						//System.out.println(utilise);
						//System.out.println(supportingDocumentIdentifiers);
						
						for(SalableProductInstanceCashRegister salableProductInstanceCashRegister : utilise)
							for(SaleCashRegisterMovement saleCashRegisterMovement : saleCashRegisterMovements)
								if(salableProductInstanceCashRegister.getSalableProductInstance().getCode().equals(saleCashRegisterMovement.getCashRegisterMovement().getMovement().getSupportingDocumentIdentifier())){
									salableProductInstanceCashRegister.setCashRegister(saleCashRegisterMovement.getSale().getCashier().getCashRegister());
								}
									
					}
					
				}
				
			}
		});
		
		CompanyWebManager.getInstance().getListeners().add(new AbstractPrimefacesManager.AbstractPrimefacesManagerListener.Adapter(){
			private static final long serialVersionUID = 1L;
			@Override
			public SystemMenu getSystemMenu(UserSession userSession) {
				return GiftCardSystemMenuBuilder.getInstance().build(userSession);
			}
		});
		
		AbstractBusinessEntityFormOnePage.BusinessEntityFormOnePageListener.COLLECTION.add(new AbstractBusinessEntityFormOnePage.BusinessEntityFormOnePageListener.Adapter.Default(Sale.class){
			private static final long serialVersionUID = 5004002889129536583L;
			@Override
			public void initialisationEnded(AbstractBean bean) {
				super.initialisationEnded(bean);
				final SaleEditPage saleEditPage = (SaleEditPage) bean;
				
				saleEditPage.getForm().getControlSetListeners().add(new ControlSetAdapter<Object>(){
					@Override
					public Boolean build(Field field) {
						if(GiftCardSystemMenuBuilder.ACTION_SELL_GIFT_CARD.equals(saleEditPage.getActionIdentifier()))
							return ArrayUtils.contains(new Object[]{SaleEditPage.FormOneSaleProduct.FIELD_CASHIER,SaleEditPage.FormOneSaleProduct.FIELD_SALABLE_PRODUCT
									,SaleEditPage.FormOneSaleProduct.FIELD_SALABLE_PRODUCT_INSTANCE,SaleEditPage.FormOneSaleProduct.FIELD_CUSTOMER}
							, field.getName());
						else if(GiftCardSystemMenuBuilder.ACTION_USE_GIFT_CARD.equals(saleEditPage.getActionIdentifier()))
							return ArrayUtils.contains(new Object[]{SaleEditPage.FormOneSaleProduct.FIELD_CASHIER,SaleEditPage.FormOneSaleProduct.FIELD_EXTERNAL_IDENTIFIER
									,SaleEditPage.FormOneSaleProduct.FIELD_SUPPORTING_DOCUMENT_IDENTIFIER
									,SaleEditPage.FormOneSaleProduct.FIELD_CASH_REGISTER_MOVEMENT_MODE}
							, field.getName());
						else
							return Boolean.FALSE;
					}
					/*@Override
					public void input(ControlSet<Object, DynaFormModel, DynaFormRow, DynaFormLabel, DynaFormControl, SelectItem> controlSet,
							Input<?, DynaFormModel, DynaFormRow, DynaFormLabel, DynaFormControl, SelectItem> input) {
						super.input(controlSet, input);
						input.setReadOnly(ArrayUtils.contains(new Object[]{SaleEditPage.FormOneSaleProduct.FIELD_COST}, input.getField().getName()));
					}*/
				});
			}
			
			@Override
			public void afterInitialisationEnded(AbstractBean bean) {
				final SaleEditPage saleEditPage = (SaleEditPage) bean;
				super.afterInitialisationEnded(bean);
				saleEditPage.setFieldValue(SaleEditPage.FormOneSaleProduct.FIELD_CASH_REGISTER_MOVEMENT_MODE, CompanyBusinessLayer.getInstance().getCashRegisterMovementModeBusiness().find(CashRegisterMovementMode.GIFT_CARD));
				
				if(GiftCardSystemMenuBuilder.ACTION_SELL_GIFT_CARD.equals(saleEditPage.getActionIdentifier())){
					
				}else if(GiftCardSystemMenuBuilder.ACTION_USE_GIFT_CARD.equals(saleEditPage.getActionIdentifier())){
					//saleEditPage.getForm().findInputByClassByFieldName(WebInput.class, FormOneSaleProduct.FIELD_CASHIER).getAjaxListener().setEnabled(Boolean.FALSE);
				}
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
