package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Collection;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.sale.SaleCashRegisterMovementDetails;
import org.cyk.system.company.business.impl.sale.SaleDetails;
import org.cyk.system.company.business.impl.sale.SaleProductDetails;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.model.sale.SaleProductInstance;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.impl.mathematics.MovementDetails;
import org.cyk.system.root.model.mathematics.MovementAction;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.ui.api.command.AbstractCommandable.Builder;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.data.collector.form.ControlSet;
import org.cyk.ui.api.model.table.Column;
import org.cyk.ui.api.model.table.ColumnAdapter;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;

@Named @ViewScoped @Getter @Setter
public class SaleConsultPage extends AbstractConsultPage<Sale> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	public static Boolean SHOW_SALE_PRODUCT_TABLE = Boolean.TRUE;
	
	@Inject private CompanyBusinessLayer companyBusinessLayer;
	@Inject private CompanyWebManager companyWebManager;
	
	private FormOneData<SaleDetails> details;
	private Table<SaleProductDetails> saleProductTable;
	private Table<SaleCashRegisterMovementDetails> saleCashRegisterMovementTable;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		details = createDetailsForm(SaleDetails.class, identifiable, new DetailsConfigurationListener.Form.Adapter<Sale,SaleDetails>(Sale.class, SaleDetails.class){
			private static final long serialVersionUID = 1L;
			@Override
			public Boolean getEnabledInDefaultTab() {
				return Boolean.TRUE;
			}
		});
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
		
		if(Boolean.TRUE.equals(SHOW_SALE_PRODUCT_TABLE))
			saleProductTable = createDetailsTable(SaleProductDetails.class, new DetailsConfigurationListener.Table.Adapter<SaleProduct, SaleProductDetails>(SaleProduct.class, SaleProductDetails.class){
				private static final long serialVersionUID = 1L;
				@Override
				public Collection<SaleProduct> getIdentifiables() {
					Collection<SaleProduct> saleProducts = CompanyBusinessLayer.getInstance().getSaleProductBusiness().findBySale(identifiable);
					return saleProducts;
				}
				
				@Override
				public Collection<SaleProductDetails> getDatas() {
					Collection<SaleProductDetails> details = super.getDatas();
					for(SaleProductDetails saleProductDetails :details){
						Collection<SaleProductInstance> saleProductInstances = CompanyBusinessLayer.getInstance().getSaleProductInstanceBusiness().findBySaleProduct(saleProductDetails.getMaster());
						saleProductDetails.setInstances(saleProductInstances.toString());
					}
					return details;
				}
				
				@Override
				public SaleProductDetails createData(SaleProduct saleProduct) {
					return super.createData(saleProduct);
				}
			});
		
		saleCashRegisterMovementTable = createDetailsTable(SaleCashRegisterMovementDetails.class,new DetailsConfigurationListener.Table.Adapter<SaleCashRegisterMovement, SaleCashRegisterMovementDetails>(SaleCashRegisterMovement.class, SaleCashRegisterMovementDetails.class){
			private static final long serialVersionUID = 1L;
			@Override
			public Collection<SaleCashRegisterMovement> getIdentifiables() {
				return CompanyBusinessLayer.getInstance().getSaleCashRegisterMovementBusiness().findBySale(identifiable);
			}
			@Override
			public Crud[] getCruds() {
				return new Crud[]{Crud.CREATE,Crud.READ};
			}
		});
		
		saleCashRegisterMovementTable.getColumnListeners().add(new ColumnAdapter(){
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
	}
	
	@Override
	protected Boolean showContextualEditCommandable() {
		return Boolean.FALSE;
	}
	
	@Override
	protected Boolean showContextualDeleteCommandable() {
		return Boolean.FALSE;
	}
	
	@Override
	protected void processIdentifiableContextualCommandable(UICommandable commandable) {
		super.processIdentifiableContextualCommandable(commandable);
		Cashier cashier = null;
		if(userSession.getUser() instanceof Person){
			cashier = CompanyBusinessLayer.getInstance().getCashierBusiness().findByPerson((Person)userSession.getUser());
			
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