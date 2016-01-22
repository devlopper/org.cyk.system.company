package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.sale.SaleStockTangibleProductMovementInputBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementInput;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.root.business.api.BusinessEntityInfos;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.api.AjaxListener;
import org.cyk.ui.web.api.AjaxListener.ListenValueMethod;
import org.cyk.ui.web.api.WebNavigationManager;
import org.cyk.ui.web.api.data.collector.control.WebInputOneCombo;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputBooleanButton;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.annotation.user.interfaces.InputTextarea;
import org.cyk.utility.common.annotation.user.interfaces.Text;
import org.cyk.utility.common.annotation.user.interfaces.Text.ValueType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractSaleStockInputCrudOnePage extends AbstractCrudOnePage<SaleStockTangibleProductMovementInput> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject protected SaleStockTangibleProductMovementInputBusiness saleStockInputBusiness;
	@Inject protected SaleBusiness saleBusiness;
	@Inject protected AccountingPeriodBusiness accountingPeriodBusiness;
	//@Inject private CustomerBusiness customerBusiness;
	@Inject protected CompanyBusinessLayer companyBusinessLayer;
	@Inject protected CompanyWebManager companyWebManager;
	
	//private List<Customer> customers;

	@Inject protected SaleCashRegisterMovementController cashRegisterController;
	protected AccountingPeriod accountingPeriod;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		accountingPeriod = accountingPeriodBusiness.findCurrent();
		//customers = new ArrayList<Customer>(customerBusiness.findAll());
		//cashRegisterController.init(new SaleCashRegisterMovement(identifiable.getSale(),new CashRegisterMovement(identifiable.getSale().getCashier().getCashRegister())),Boolean.TRUE);
		previousUrl = WebNavigationManager.getInstance().url(previousUrlOutcome(),previousUrlParameters(),Boolean.FALSE,Boolean.FALSE);
		
	}
	
	protected String previousUrlOutcome(){
		return "saleStockInputListView";
	}
	
	protected Object[] previousUrlParameters(){
		return null;
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		ajax();
		form.findInputByClassByFieldName(org.cyk.ui.api.data.collector.control.InputChoice.class, "customer").setFiltered(Boolean.TRUE);
		//form.findInputByClassByFieldName(InputNumber.class,"quantity").setMinimum(-5);
		//form.findInputByClassByFieldName(InputNumber.class,"quantity").setMaximum(150);
	}
	
	protected void ajax(){
		onComplete(inputRowVisibility("valueAddedTax",Boolean.FALSE));
		priceAjaxListener();
		commissionAjaxListener();
		valueAddedTaxableAjaxListener();
	}
	
	protected AjaxListener priceAjaxListener(){
		/*return setAjaxListener("price", "change", new String[]{"commission"},new String[]{"totalCost","valueAddedTax"}, BigDecimal.class,new ListenValueMethod<BigDecimal>() {
			@Override
			public void execute(BigDecimal value) {
				updateOutputTotalCost();
			}
		});*/
		return null;
	}
	
	protected AjaxListener commissionAjaxListener(){
		/*return setAjaxListener("commission", "change", new String[]{"price"},new String[]{"totalCost","valueAddedTax"}, BigDecimal.class,new ListenValueMethod<BigDecimal>() {
			@Override
			public void execute(BigDecimal value) {
				updateOutputTotalCost();
			}
		});*/
		return null;
	}
	
	protected AjaxListener valueAddedTaxableAjaxListener(){
		/*return setAjaxListener("valueAddedTaxable", "change", null,null, Boolean.class,new ListenValueMethod<Boolean>() {
			@Override
			public void execute(Boolean value) {
				onComplete(inputRowVisibility("valueAddedTax",value));
			}
		});*/
		return null;
	}
	
	private void updateOutputTotalCost(){
		BigDecimal price = bigDecimalValue("price");
		BigDecimal commission = bigDecimalValue("commission");
		logTrace("Update total cost. Price={} , Commission={}",price,commission);
		BigDecimal totalCost = price.add(numberBusiness.computePercentage(price,commission));
		setFieldValue("totalCost", totalCost);
		BigDecimal valueAddedTax = accountingPeriodBusiness.computeValueAddedTax(accountingPeriod, totalCost);
		setFieldValue("valueAddedTax", valueAddedTax);
		logTrace("Total cost updated {}",totalCost);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected <T extends AbstractIdentifiable> T identifiableFromRequestParameter(Class<T> aClass) {
		if(Crud.CREATE.equals(crudFromRequestParameter()))
			return (T) null;//saleStockInputBusiness.instanciate((Person) getUserSession().getUser());
		return super.identifiableFromRequestParameter(aClass);
	}
	
	@Override
	protected Crud crudFromRequestParameter() {
		return Crud.CREATE;
	}
	
	@Override
	protected BusinessEntityInfos fetchBusinessEntityInfos() {
		return uiManager.businessEntityInfos(SaleStockTangibleProductMovementInput.class);
	}
	
	@Override
	protected Class<? extends AbstractFormModel<?>> __formModelClass__() {
		return SaleStockInputFormModel.class;
	}
	
	@Override
	protected void create() {
		/*SaleProduct saleProduct = identifiable.getSale().getSaleProducts().iterator().next();
		saleBusiness.applyChange(identifiable.getSale(), saleProduct);
		saleStockInputBusiness.create(identifiable, cashRegisterController.getSaleCashRegisterMovement());*/
	}
	
	@Override
	public Object succeed(UICommand command, Object parameter) {
		super.succeed(command, parameter);
		//messageDialogOkButtonOnClick = javaScriptHelper.add(messageDialogOkButtonOnClick, companyWebManager.javascriptShowPointOfSale(identifiable.getSale()));
		return null;
	}
	
	/**/
	
	@Getter @Setter
	public static class SaleStockInputFormModel extends AbstractFormModel<SaleStockTangibleProductMovementInput> implements Serializable {

		private static final long serialVersionUID = -7403076234556118486L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private Customer customer;
		
		@Input(label=@Text(type=ValueType.ID,value="ui.form.salestockinput.field.external.identifier")) 
		@InputText @NotNull private String externalIdentifier;
		
		@Input(label=@Text(type=ValueType.ID,value="ui.form.salestockinput.field.comments")) @InputTextarea @NotNull
		private String comments;
		
		@Input @InputNumber @NotNull //@Size(min=1,max=Integer.MAX_VALUE) 
		private BigDecimal quantity;
		
		@Input @InputNumber @NotNull //@Size(min=0,max=Integer.MAX_VALUE)
		private BigDecimal price;
		
		private Boolean commissionInPercentage = Boolean.TRUE;
		
		@Input @InputNumber @NotNull //@Size(min=0,max=Integer.MAX_VALUE)
		private BigDecimal commission;
		
		@Input @InputBooleanButton @NotNull //@Size(min=0,max=Integer.MAX_VALUE)
		private Boolean valueAddedTaxable;
		
		@Input(readOnly=true) @InputText @NotNull 
		private String totalCost; // used for read only value
		
		@Input(readOnly=true) @InputText @NotNull 
		private String valueAddedTax; // used for read only value
		
		private SaleProduct saleProduct;
		
		@Override
		public void read() {
			super.read();
			/*this.customer = identifiable.getSale().getCustomer();
			this.externalIdentifier = identifiable.getExternalIdentifier();
			//this.price = identifiable.getSale().getCost();
			this.quantity = identifiable.getTangibleProductStockMovement().getQuantity();
			this.saleProduct = identifiable.getSale().getSaleProducts().iterator().next();
			this.commission = saleProduct.getCommission();
			//this.valueAddedTaxable = BigDecimal.ZERO.compareTo(identifiable.getSale().getValueAddedTax()) != 0;
			//this.valueAddedTax = numberBusiness.format(identifiable.getSale().getValueAddedTax());
			this.comments = identifiable.getSale().getComments();*/
		}
		
		@Override
		public void write() {
			super.write();
			/*identifiable.getSale().setCustomer(customer);
			identifiable.setExternalIdentifier(externalIdentifier);
			identifiable.getSale().setAutoComputeValueAddedTax(valueAddedTaxable);
			identifiable.getSale().setComments(comments);
			if(Boolean.TRUE.equals(commissionInPercentage)){
				saleProduct.setCommission(numberBusiness.computePercentage(price,commission));
			}else
				saleProduct.setCommission(commission);
			identifiable.getTangibleProductStockMovement().setQuantity(quantity);
			SaleProduct saleProduct = identifiable.getSale().getSaleProducts().iterator().next();*/
			//saleProduct.setPrice(price);
		}
	}
	
}