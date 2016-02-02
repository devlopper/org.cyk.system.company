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
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.impl.mathematics.MovementDetails;
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
		
		saleProductTable = createDetailsTable(SaleProductDetails.class, new DetailsConfigurationListener.Table.Adapter<SaleProduct, SaleProductDetails>(SaleProduct.class, SaleProductDetails.class){
			private static final long serialVersionUID = 1L;
			@Override
			public Collection<SaleProduct> getIdentifiables() {
				return CompanyBusinessLayer.getInstance().getSaleProductBusiness().findBySale(identifiable);
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
	protected void processIdentifiableContextualCommandable(UICommandable commandable) {
		super.processIdentifiableContextualCommandable(commandable);
		UICommandable c;
		Integer balance = identifiable.getBalance().getValue().compareTo(BigDecimal.ZERO);
		if(balance!=0){
			if(balance>0){
				commandable.addChild(c = navigationManager.createCreateCommandable(identifiable,SaleCashRegisterMovement.class, "command.pay", null));
				c.addParameter(CompanyWebManager.getInstance().getRequestParameterPaymentType(), CompanyWebManager.getInstance().getRequestParameterPay());
			}else{
				commandable.addChild(c = navigationManager.createCreateCommandable(identifiable,SaleCashRegisterMovement.class, "command.payback", null));
				c.addParameter(CompanyWebManager.getInstance().getRequestParameterPaymentType(), CompanyWebManager.getInstance().getRequestParameterPayback());
			}
		}
		commandable.addChild(navigationManager.createReportCommandable(identifiable, CompanyReportRepository.getInstance().getReportPointOfSale()
				,"command.see.invoice", null));
	}
				
}