package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.sale.SalableProductCollectionItemDetails;
import org.cyk.system.company.business.impl.sale.SaleCashRegisterMovementDetails;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.root.model.mathematics.MovementAction;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.ui.api.command.AbstractCommandable.Builder;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;

@Named @ViewScoped @Getter @Setter
public class SaleConsultPageOLD extends AbstractConsultPage<Sale> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	public static Boolean SHOW_SALE_PRODUCT_TABLE = Boolean.TRUE;
	
	@Inject private CompanyBusinessLayer companyBusinessLayer;
	@Inject private CompanyWebManager companyWebManager;
	
	private Table<SalableProductCollectionItemDetails> saleProductTable;
	private Table<SaleCashRegisterMovementDetails> saleCashRegisterMovementTable;
	/*
	@Override@Getter @Setter
	public abstract class AbstractMovementCollectionEditPage<COLLECTION extends AbstractIdentifiable> extends AbstractCollectionEditPage<COLLECTION> implements Serializable {

		private static final long serialVersionUID = 3274187086682750183L;
		
		protected abstract MovementCollection getMovementCollection();
		
		@Override
		protected AbstractCollection<?> getCollection() {
			return getMovementCollection();
		}
		
		@Getter @Setter
		public static abstract class AbstractMovementCollectionForm<COLLECTION extends AbstractIdentifiable> extends AbstractForm<COLLECTION> implements Serializable{
			private static final long serialVersionUID = -4741435164709063863L;
			
			protected abstract MovementCollection getMovementCollection();
			
			@Override
			protected AbstractCollection<?> getCollection() {
				return getMovementCollection();
			}
			
			
			@Getter @Setter
			public static abstract class Default<COLLECTION extends AbstractCollection<?>> extends AbstractMovementCollectionForm<COLLECTION> implements Serializable{
				private static final long serialVersionUID = -4741435164709063863L;
				
				
				
			}
			
		}
		
		@Getter @Setter
		public static abstract class AbstractDefaultForm<COLLECTION extends AbstractCollection<?>> extends AbstractForm.Default<COLLECTION> implements Serializable{
			private static final long serialVersionUID = -4741435164709063863L;

			
		}

	}*/

	protected void initialisation() {
		super.initialisation();
		/*
		details.getControlSetListeners().add(new ControlSetAdapter<SaleDetails>(){
			@Override
			public String fiedLabel(
					ControlSet<SaleDetails, DynaFormModel, DynaFormRow, DynaFormLabel, DynaFormControl, SelectItem> controlSet,
					Field field) {
				if(field.getName().equals("balance"))
					if(identifiable.getBalance().getValue().signum()==1)
						return text("field.reminder.to.pay");
					else if(identifiable.getBalance().getValue().signum()==-1)
						return text("field.amount.to.payback");
				return super.fiedLabel(controlSet, field);
			}
		}); 
		*/
		
		/*
		if(Boolean.TRUE.equals(SHOW_SALE_PRODUCT_TABLE))
			saleProductTable = createDetailsTable(SalableProductCollectionItemDetails.class, new DetailsConfigurationListener.Table.Adapter<SaleProduct, SalableProductCollectionItemDetails>(SaleProduct.class, SalableProductCollectionItemDetails.class){
				private static final long serialVersionUID = 1L;
				@Override
				public Collection<SaleProduct> getIdentifiables() {
					Collection<SaleProduct> saleProducts = inject(SaleProductBusiness.class).findBySale(identifiable);
					return saleProducts;
				}
				
				@Override
				public Collection<SalableProductCollectionItemDetails> getDatas() {
					Collection<SalableProductCollectionItemDetails> details = super.getDatas();
					for(SalableProductCollectionItemDetails saleProductDetails :details){
						Collection<SaleProductInstance> saleProductInstances = inject(SaleProductInstanceBusiness.class).findBySaleProduct(saleProductDetails.getMaster());
						saleProductDetails.setInstances(saleProductInstances.toString());
					}
					return details;
				}
				
				@Override
				public SalableProductCollectionItemDetails createData(SaleProduct saleProduct) {
					return super.createData(saleProduct);
				}
			});
		
		saleCashRegisterMovementTable = createDetailsTable(SaleCashRegisterMovementDetails.class,new DetailsConfigurationListener.Table.Adapter<SaleCashRegisterMovement, SaleCashRegisterMovementDetails>(SaleCashRegisterMovement.class, SaleCashRegisterMovementDetails.class){
			private static final long serialVersionUID = 1L;
			@Override
			public Collection<SaleCashRegisterMovement> getIdentifiables() {
				return inject(SaleCashRegisterMovementBusiness.class).findBySale(identifiable);
			}
			@Override
			public Crud[] getCruds() {
				return new Crud[]{Crud.CREATE,Crud.READ};
			}
		});
		
		saleCashRegisterMovementTable.getColumnListeners().add(new ColumnAdapter(){
			private static final long serialVersionUID = 1L;
			@Override
			public Boolean isColumn(Field field) {
				return !ArrayUtils.contains(SaleCashRegisterMovementDetails.getFieldsToHide(), field.getName());
			}
			@Override
			public void added(Column column) {
				super.added(column);
				if(column.getField().getName().equals(MovementDetails.FIELD_VALUE))
					column.setTitle(text("field.amount"));
			}
		});
		
		*/
	}
	
	@Override
	protected Boolean showContextualEditCommandable() {
		return Boolean.FALSE;
	}
	
	@Override
	protected Boolean showContextualDeleteCommandable() {
		return Boolean.TRUE;
	}
	
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
	}
				
}