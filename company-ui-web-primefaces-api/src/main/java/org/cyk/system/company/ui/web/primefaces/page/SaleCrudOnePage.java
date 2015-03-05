package org.cyk.system.company.ui.web.primefaces.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.api.product.CustomerBusiness;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaledProduct;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;

@Named @ViewScoped @Getter @Setter
public class SaleCrudOnePage extends AbstractCrudOnePage<Sale> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private SaleBusiness saleBusiness;
	@Inject private SaleCashRegisterMovementBusiness paymentBusiness;
	@Inject private ProductBusiness productBusiness;
	@Inject private CustomerBusiness customerBusiness;
	@Inject private CashierBusiness cashierBusiness;
	@Inject private CompanyBusinessLayer companyBusinessLayer;
	
	private List<Product> products;
	private List<Customer> customers;

	private Product selectedProduct;
	private Customer selectedCustomer;
	
	private Boolean collectProduct=Boolean.FALSE,collectMoney=Boolean.TRUE,showQuantityColumn = Boolean.TRUE;
	
	@Inject private SaleCashRegisterMovementController cashRegisterController;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		products = new ArrayList<Product>(productBusiness.findAll());
		customers = new ArrayList<Customer>(customerBusiness.findAll());
		identifiable.setCashier(cashierBusiness.find().one());//Must be found by employee / user session
		cashRegisterController.init(new SaleCashRegisterMovement(identifiable,new CashRegisterMovement(identifiable.getCashier().getCashRegister())),Boolean.TRUE);
		sell();
	}
	
	/**/
	
	@Override
	protected void create() {
		saleBusiness.create(identifiable, cashRegisterController.getSaleCashRegisterMovement());
		
	}
	
	@Override
	public Object succeed(UICommand command, Object parameter) {
		super.succeed(command, parameter);
		String url = null;
		//url = "http://localhost:8080/company/private/__tools__/report.jsf?clazz=Sale&identifiable=151&fileExtensionParam=pdf&ridp=pos&windowmode=windowmodedialog";
		url = navigationManager.reportUrl(identifiable, companyBusinessLayer.getReportPointOfSale(),uiManager.getPdfParameter(),Boolean.TRUE);
		messageDialogOkButtonOnClick += "window.open('"+url+"', 'pointofsale"+identifiable.getIdentifier()+"', 'location=no,menubar=no,titlebar=no,toolbar=no,width=400, height=550')";
		//System.out.println(messageDialogOkButtonOnClick);
		return null;
	}
	
	public void addProduct(){
		saleBusiness.selectProduct(identifiable, selectedProduct);
	}
	
	public void deleteProduct(SaledProduct saledProduct){
		saleBusiness.unselectProduct(identifiable, saledProduct);
	}
	
	public void cash(){
		collectProduct = Boolean.FALSE;
		collectMoney = Boolean.TRUE;
	}
	
	public void sell(){
		collectProduct = Boolean.TRUE;
		collectMoney = Boolean.FALSE;
	}
		
	public void productQuantityChanged(SaledProduct saledProduct){
		saleBusiness.quantifyProduct(identifiable, saledProduct);
	}
	
}