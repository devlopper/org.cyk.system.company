package org.cyk.system.company.ui.web.primefaces.page.product;

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

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.product.ProductEmployee;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.root.business.api.Crud;
import org.cyk.ui.api.UIProvider;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.UICommandable.CommandRequestType;
import org.cyk.ui.api.command.UICommandable.IconType;
import org.cyk.ui.api.command.UICommandable.Parameter;
import org.cyk.ui.api.command.UICommandable.ViewType;
import org.cyk.ui.api.data.collector.form.ControlSet;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;

@Named @ViewScoped @Getter @Setter
public class SaleConsultPage extends AbstractConsultPage<Sale> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private CompanyBusinessLayer companyBusinessLayer;
	@Inject private CompanyWebManager companyWebManager;
	
	private FormOneData<SaleDetails> saleDetails;
	private Table<ProductDetails> productTable;
	private Table<ProductExecutionDetails> productExecutionTable;
	private Table<PaymentDetails> paymentTable;
	
	private UICommandable editExecutionDetails;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void initialisation() {
		super.initialisation();
		contentTitle += " - "+identifiable.getIdentificationNumber();
		
		saleDetails = (FormOneData<SaleDetails>) createFormOneData(new SaleDetails(identifiable), Crud.READ);
		configureDetailsForm(saleDetails);
		saleDetails.setControlSetListener(new ControlSetAdapter<SaleDetails>(){
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
		
		productTable = (Table<ProductDetails>) createTable(ProductDetails.class, null, null);
		configureDetailsTable(productTable, "model.entity.product");
		
		productExecutionTable = (Table<ProductExecutionDetails>) createTable(ProductExecutionDetails.class, null, null);
		configureDetailsTable(productExecutionTable, /*"executiondetails"*/null);
		
		paymentTable = (Table<PaymentDetails>) createTable(PaymentDetails.class, null, null);
		configureDetailsTable(paymentTable, "model.entity.payment");
		
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		for(SaleProduct saleProduct : identifiable.getSaleProducts())
			productTable.addRow(new ProductDetails(saleProduct));
		
		for(ProductEmployee productEmployee : identifiable.getPerformers())
			productExecutionTable.addRow(new ProductExecutionDetails(productEmployee));
		
		for(SaleCashRegisterMovement payment : identifiable.getSaleCashRegisterMovements())
			paymentTable.addRow(new PaymentDetails(payment));
		
		productTable.getColumn("price").setFooter(numberBusiness.format(identifiable.getCost()));
		paymentTable.getColumn("paid").setFooter(numberBusiness.format(identifiable.getCost().subtract(identifiable.getBalance().getValue())));
		
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
		
		editExecutionDetails = UIProvider.getInstance().createCommandable("command.edit.executiondetails", IconType.ACTION_EDIT);
		editExecutionDetails.setCommandRequestType(CommandRequestType.UI_VIEW);
		editExecutionDetails.setViewId(CompanyWebManager.getInstance().getOutcomeEditSaleDeliveryDetails());
		editExecutionDetails.getParameters().add(new Parameter(webManager.getRequestParameterIdentifiable(), identifiable.getIdentifier()));
		editExecutionDetails.getParameters().add(new Parameter(webManager.getRequestParameterPreviousUrl(), url));
		contextualMenu.getChildren().add(editExecutionDetails);
		
		UICommandable printReceipt = UIProvider.getInstance().createCommandable("command.see.receipt", null);
		printReceipt.setCommandRequestType(CommandRequestType.UI_VIEW);
		printReceipt.setViewType(ViewType.TOOLS_REPORT);
		printReceipt.getParameters().addAll(navigationManager.reportParameters(identifiable, companyBusinessLayer.getReportPointOfSale(),Boolean.FALSE));
		contextualMenu.getChildren().add(printReceipt);
		
		return Arrays.asList(contextualMenu);
	}
	
	/**/
	
	@Getter @Setter
	private class SaleDetails implements Serializable {
		private static final long serialVersionUID = -1498269103849317057L;
		
		@Input @InputText
		private String identifier,cost,balance,customer,date;
		
		public SaleDetails(Sale sale) {
			this.identifier = sale.getIdentificationNumber();
			this.cost = numberBusiness.format(sale.getCost());
			this.balance = numberBusiness.format(sale.getBalance().getValue().abs());
			this.customer = sale.getCustomer()==null?"":sale.getCustomer().getPerson().getNames();
			this.date = timeBusiness.formatDateTime(sale.getDate());
		}
	}
	
	@Getter @Setter
	private class ProductDetails implements Serializable {
		private static final long serialVersionUID = -1498269103849317057L;
		
		@Input @InputText
		private String name,unitPrice,quantity,price;
		
		public ProductDetails(SaleProduct saleProduct) {
			this.name = saleProduct.getProduct().getCode()+" - "+saleProduct.getProduct().getName();
			this.unitPrice = saleProduct.getProduct().getPrice()==null?"":numberBusiness.format(saleProduct.getProduct().getPrice());
			this.quantity = numberBusiness.format(saleProduct.getQuantity());
			this.price = numberBusiness.format(saleProduct.getPrice());
		}
	}
	
	@Getter @Setter
	private class ProductExecutionDetails implements Serializable {
		private static final long serialVersionUID = -1498269103849317057L;
		
		@Input @InputText
		private String name,deliveredBy,comments;
		
		public ProductExecutionDetails(ProductEmployee productEmployee) {
			this.name = productEmployee.getProduct().getCode()+" - "+productEmployee.getProduct().getName();
			this.deliveredBy = productEmployee.getEmployee().getPerson().getNames();
			this.comments = productEmployee.getComments();
		}
	}
	
	@Getter @Setter
	private class PaymentDetails implements Serializable {
		private static final long serialVersionUID = -6341285110719947720L;
		
		@Input @InputText
		private String identifier,paid,date;
		
		public PaymentDetails(SaleCashRegisterMovement payment) {
			this.identifier = payment.getCashRegisterMovement().getIdentificationNumber();
			this.paid = numberBusiness.format(payment.getCashRegisterMovement().getAmount());
			this.date = timeBusiness.formatDateTime(payment.getCashRegisterMovement().getDate());
		}
	}
	
	@Override
	public Boolean getShowContextualMenu() {
		return Boolean.TRUE;
	}
	
}