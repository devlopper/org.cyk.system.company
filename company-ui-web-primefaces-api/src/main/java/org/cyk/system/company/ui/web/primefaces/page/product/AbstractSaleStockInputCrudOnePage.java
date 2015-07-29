package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.inject.Inject;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaleStockInputBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.company.ui.web.primefaces.model.SaleStockInputFormModel;
import org.cyk.system.root.business.api.BusinessEntityInfos;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.api.AjaxListener;
import org.cyk.ui.web.api.AjaxListener.ListenValueMethod;
import org.cyk.ui.web.api.WebNavigationManager;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractSaleStockInputCrudOnePage extends AbstractCrudOnePage<SaleStockInput> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject protected SaleStockInputBusiness saleStockInputBusiness;
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
		cashRegisterController.init(new SaleCashRegisterMovement(identifiable.getSale(),new CashRegisterMovement(identifiable.getSale().getCashier().getCashRegister())),Boolean.TRUE);
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
		return setAjaxListener("price", "change", new String[]{"commission"},new String[]{"totalCost","valueAddedTax"}, BigDecimal.class,new ListenValueMethod<BigDecimal>() {
			@Override
			public void execute(BigDecimal value) {
				updateOutputTotalCost();
			}
		});
	}
	
	protected AjaxListener commissionAjaxListener(){
		return setAjaxListener("commission", "change", new String[]{"price"},new String[]{"totalCost","valueAddedTax"}, BigDecimal.class,new ListenValueMethod<BigDecimal>() {
			@Override
			public void execute(BigDecimal value) {
				updateOutputTotalCost();
			}
		});
	}
	
	protected AjaxListener valueAddedTaxableAjaxListener(){
		return setAjaxListener("valueAddedTaxable", "change", null,null, Boolean.class,new ListenValueMethod<Boolean>() {
			@Override
			public void execute(Boolean value) {
				onComplete(inputRowVisibility("valueAddedTax",value));
			}
		});
	}
	
	private void updateOutputTotalCost(){
		BigDecimal price = bigDecimalValue("price");
		BigDecimal commission = bigDecimalValue("commission");
		BigDecimal totalCost = price.add(numberBusiness.computePercentage(price,commission));
		setFieldValue("totalCost", totalCost);
		BigDecimal valueAddedTax = accountingPeriodBusiness.computeValueAddedTax(accountingPeriod, totalCost);
		setFieldValue("valueAddedTax", valueAddedTax);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected <T extends AbstractIdentifiable> T identifiableFromRequestParameter(Class<T> aClass) {
		if(Crud.CREATE.equals(crudFromRequestParameter()))
			return (T) saleStockInputBusiness.newInstance((Person) getUserSession().getUser());
		return super.identifiableFromRequestParameter(aClass);
	}
	
	@Override
	protected Crud crudFromRequestParameter() {
		return Crud.CREATE;
	}
	
	@Override
	protected BusinessEntityInfos fetchBusinessEntityInfos() {
		return uiManager.businessEntityInfos(SaleStockInput.class);
	}
	
	@Override
	protected Class<? extends AbstractFormModel<?>> __formModelClass__() {
		return SaleStockInputFormModel.class;
	}
	
	@Override
	protected void create() {
		SaleProduct saleProduct = identifiable.getSale().getSaleProducts().iterator().next();
		saleBusiness.applyChange(identifiable.getSale(), saleProduct);
		saleStockInputBusiness.create(identifiable, cashRegisterController.getSaleCashRegisterMovement());
	}
	
	@Override
	public Object succeed(UICommand command, Object parameter) {
		super.succeed(command, parameter);
		messageDialogOkButtonOnClick = javaScriptHelper.add(messageDialogOkButtonOnClick, companyWebManager.javascriptShowPointOfSale(identifiable.getSale()));
		return null;
	}
	
	/**/
	
}