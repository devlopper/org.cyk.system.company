package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.product.CustomerBusiness;
import org.cyk.system.company.business.api.product.IntangibleProductBusiness;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;

@Named @ViewScoped @Getter @Setter
public class SaleCrudOnePage extends AbstractCrudOnePage<Sale> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private SaleBusiness saleBusiness;
	@Inject private ProductBusiness productBusiness;
	@Inject private IntangibleProductBusiness intangibleProductBusiness;
	@Inject private TangibleProductBusiness tangibleProductBusiness;
	@Inject private CustomerBusiness customerBusiness;
	@Inject private CompanyBusinessLayer companyBusinessLayer;
	
	private List<Product> products,intangibleProducts,tangibleProducts;
	private List<Customer> customers;

	private Product selectedProduct,selectedIntangibleProduct,selectedTangibleProduct;
	private Customer selectedCustomer;
	
	private Boolean collectProduct=Boolean.FALSE,collectMoney=Boolean.TRUE,showQuantityColumn = Boolean.TRUE;
	
	@Inject private SaleCashRegisterMovementController cashRegisterController;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		/*
		identifiable.setAccountingPeriod(accountingPeriodBusiness.findCurrent());
		identifiable.setCashier(cashierBusiness.findByPerson((Person) getUserSession().getUser()));
		if(identifiable.getCashier()==null){
			renderViewErrorMessage("View Init Error!!!", "View Init Error Details!!!");
			return;
		}
		*/
		products = new ArrayList<Product>(productBusiness.findBySalable(Boolean.TRUE));
		intangibleProducts = new ArrayList<Product>(intangibleProductBusiness.findBySalable(Boolean.TRUE));
		tangibleProducts = new ArrayList<Product>(tangibleProductBusiness.findBySalable(Boolean.TRUE));
		customers = new ArrayList<Customer>(customerBusiness.findAll());
		cashRegisterController.init(new SaleCashRegisterMovement(identifiable,new CashRegisterMovement(identifiable.getCashier().getCashRegister())),Boolean.TRUE);
		sell();
	}
	
	/**/
	
	@SuppressWarnings("unchecked")
	@Override
	protected <T extends AbstractIdentifiable> T identifiableFromRequestParameter(Class<T> aClass) {
		return (T) saleBusiness.newInstance((Person) getUserSession().getUser()); //super.identifiableFromRequestParameter(aClass);
	}
	
	@Override
	protected void create() {
		identifiable.setCustomer(selectedCustomer);
		saleBusiness.create(identifiable, cashRegisterController.getSaleCashRegisterMovement());
	}
	
	@Override
	public Object succeed(UICommand command, Object parameter) {
		super.succeed(command, parameter);
		String url = null;
		//url = "http://localhost:8080/company/private/__tools__/report.jsf?clazz=Sale&identifiable=151&fileExtensionParam=pdf&ridp=pos&windowmode=windowmodedialog";
		url = navigationManager.reportUrl(identifiable, CompanyReportRepository.getInstance().getReportPointOfSale(),uiManager.getPdfParameter(),Boolean.TRUE);
		messageDialogOkButtonOnClick += "window.open('"+url+"', 'pointofsale"+identifiable.getIdentifier()+"', 'location=no,menubar=no,titlebar=no,toolbar=no,width=400, height=550');";
		//System.out.println(messageDialogOkButtonOnClick);
		return null;
	}
	
	public void addProduct(Integer type){
		if(type==0)
			saleBusiness.selectProduct(identifiable, selectedProduct);
		else if(type==1)
			saleBusiness.selectProduct(identifiable, selectedIntangibleProduct);
		else if(type==2)
			saleBusiness.selectProduct(identifiable, selectedTangibleProduct);
	}
	
	public void deleteProduct(SaleProduct saleProduct){
		saleBusiness.unselectProduct(identifiable, saleProduct);
	}
	
	public void cash(){
		collectProduct = Boolean.FALSE;
		collectMoney = Boolean.TRUE;
	}
	
	public void sell(){
		collectProduct = Boolean.TRUE;
		collectMoney = Boolean.FALSE;
	}
		
	public void productQuantityChanged(SaleProduct saleProduct){
		saleBusiness.applyChange(identifiable, saleProduct);
	}
	
	public void productPriceChanged(SaleProduct saleProduct){
		saleBusiness.applyChange(identifiable, saleProduct);
	}
	
}