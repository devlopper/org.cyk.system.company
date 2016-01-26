package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.sale.PaymentDetails;
import org.cyk.system.company.business.impl.sale.SaleDetails;
import org.cyk.system.company.business.impl.sale.SaleProductDetails;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.ui.api.UIProvider;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.UICommandable.CommandRequestType;
import org.cyk.ui.api.command.UICommandable.Parameter;
import org.cyk.ui.api.command.UICommandable.ViewType;
import org.cyk.ui.api.data.collector.form.ControlSet;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class SaleConsultPage extends AbstractConsultPage<Sale> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private CompanyBusinessLayer companyBusinessLayer;
	@Inject private CompanyWebManager companyWebManager;
	
	private FormOneData<SaleDetails> details;
	private Table<SaleProductDetails> productTable;
	private Table<PaymentDetails> paymentTable;
	
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
		
		productTable = createDetailsTable(SaleProductDetails.class, new DetailsConfigurationListener.Table.Adapter<SaleProduct, SaleProductDetails>(SaleProduct.class, SaleProductDetails.class){
			private static final long serialVersionUID = 1L;
			
		});
		
		
		//productExecutionTable = (Table<ProductExecutionDetails>) createTable(ProductExecutionDetails.class, null, null);
		//configureDetailsTable(productExecutionTable, /*"executiondetails"*/null);
		
		paymentTable = createDetailsTable(PaymentDetails.class,new DetailsConfigurationListener.Table.Adapter<SaleCashRegisterMovement, PaymentDetails>(SaleCashRegisterMovement.class, PaymentDetails.class){
			private static final long serialVersionUID = 1L;
			
		});
		//configureDetailsTable(paymentTable, "model.entity.payment");
		
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		for(SaleProduct saleProduct : identifiable.getSaleProducts())
			productTable.addRow(new SaleProductDetails(saleProduct));
		/*
		for(ProductEmployee productEmployee : identifiable.getPerformers())
			productExecutionTable.addRow(new ProductExecutionDetails(productEmployee));
		*/
		for(SaleCashRegisterMovement payment : identifiable.getSaleCashRegisterMovements())
			paymentTable.addRow(new PaymentDetails(payment));
		
		//productTable.getColumn("price").setFooter(numberBusiness.format(identifiable.getCost()));
		//paymentTable.getColumn("paid").setFooter(numberBusiness.format(identifiable.getCost().subtract(identifiable.getBalance().getValue())));
		
	}
	
	@Override
	protected Collection<UICommandable> contextualCommandables() {
		Integer balance = identifiable.getBalance().getValue().compareTo(BigDecimal.ZERO);
		UICommandable contextualMenu = UIProvider.getInstance().createCommandable("button", null);
		contextualMenu.setLabel(contentTitle); 
		if(balance!=0){
				
			Collection<Parameter> parameters = Arrays.asList(new UICommandable.Parameter(uiManager.getClassParameter(), uiManager.keyFromClass(SaleCashRegisterMovement.class)),
					new Parameter(uiManager.getCrudParameter(), uiManager.getCrudCreateParameter())
			,new UICommandable.Parameter(uiManager.keyFromClass(Sale.class), identifiable.getIdentifier()));
			Collection<Parameter> p  = new ArrayList<>(parameters);
			p.add(new Parameter(webManager.getRequestParameterPreviousUrl(), url));
			if(balance>0){
				p.add(new Parameter(CompanyWebManager.getInstance().getRequestParameterPaymentType(), CompanyWebManager.getInstance().getRequestParameterPay() ));
				contextualMenu.addChild("command.pay", null, "paymentEditView", p);	
			}else{
				p.add(new Parameter(CompanyWebManager.getInstance().getRequestParameterPaymentType(), CompanyWebManager.getInstance().getRequestParameterPayback() ));
				contextualMenu.addChild("command.payback", null, "paymentEditView", p);	
			}
		}
	
		UICommandable printReceipt = UIProvider.getInstance().createCommandable("command.see.invoice", null);
		printReceipt.setCommandRequestType(CommandRequestType.UI_VIEW);
		printReceipt.setViewType(ViewType.TOOLS_REPORT);
		printReceipt.getParameters().addAll(navigationManager.reportParameters(identifiable, CompanyReportRepository.getInstance().getReportPointOfSale(),Boolean.FALSE));
		contextualMenu.getChildren().add(printReceipt);
		
		return Arrays.asList(contextualMenu);
	}
	
	/**/
	
	@Override
	public Boolean getShowContextualMenu() {
		return Boolean.TRUE;
	}
	
}