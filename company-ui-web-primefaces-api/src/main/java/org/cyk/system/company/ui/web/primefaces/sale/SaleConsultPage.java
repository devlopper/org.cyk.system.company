package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.sale.SalableProductCollectionItemBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.sale.SalableProductCollectionItemDetails;
import org.cyk.system.company.business.impl.sale.SaleCashRegisterMovementDetails;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.ui.web.primefaces.Table;

@Named @ViewScoped @Getter @Setter
public class SaleConsultPage extends AbstractSalableProductCollectionConsultPage<Sale,SalableProductCollectionItem,SalableProductCollectionItemDetails> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	public static Boolean SHOW_SALE_PRODUCT_TABLE = Boolean.TRUE;
	
	@Inject private CompanyBusinessLayer companyBusinessLayer;
	@Inject private CompanyWebManager companyWebManager;
	
	private Table<SaleCashRegisterMovementDetails> saleCashRegisterMovementTable;
	
	@Override
	protected void consultInitialisation() {
		super.consultInitialisation();
		saleCashRegisterMovementTable = createDetailsTable(SaleCashRegisterMovementDetails.class,new DetailsConfigurationListener.Table.Adapter<SaleCashRegisterMovement, SaleCashRegisterMovementDetails>(SaleCashRegisterMovement.class, SaleCashRegisterMovementDetails.class){
			private static final long serialVersionUID = 1L;
			@Override
			public Collection<SaleCashRegisterMovement> getIdentifiables() {
				return inject(SaleCashRegisterMovementBusiness.class).findBySale(identifiable);
			}
		});
		
	}
	
	@Override
	protected SalableProductCollection getSalableProductCollection() {
		return identifiable.getSalableProductCollection();
	}
	
	@Override
	protected Collection<SalableProductCollectionItem> findByCollection(Sale sale) {
		return inject(SalableProductCollectionItemBusiness.class).findByCollection(sale.getSalableProductCollection());
	}
	
	/*
	@Override
	protected void processIdentifiableContextualCommandable(UICommandable commandable) {
		super.processIdentifiableContextualCommandable(commandable);
		Cashier cashier = null;
		if(userSession.getUser() instanceof Person){
			cashier = inject(CashierBusiness.class).findByPerson((Person)userSession.getUser());
			
			UICommandable c;
			Integer balance = identifiable.getBalance().getValue().compareTo(BigDecimal.ZERO);
			if(balance!=0){
				MovementCollection movementCollection = cashier==null?null:cashier.getCashRegister().getMovementCollection();
				MovementAction action;
				if(balance>0){
					action = movementCollection.getIncrementAction();
					
				}else{
					action = movementCollection.getDecrementAction();
				}
				commandable.addChild(c = Builder.createCreate(identifiable,SaleCashRegisterMovement.class, action.getName(), null));
				c.addParameter(uiManager.businessEntityInfos(MovementAction.class).getIdentifier(), action.getIdentifier());
				c.setLabel(action.getName());
			}
		}
		
		commandable.addChild(Builder.createReport(identifiable, CompanyReportRepository.getInstance().getReportPointOfSale(),"command.see.invoice", null));
	}*/
				
}