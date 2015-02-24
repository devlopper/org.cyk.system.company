package org.cyk.system.company.ui.web.primefaces.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.product.CustomerBusiness;
import org.cyk.system.company.business.api.product.PaymentBusiness;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.Payment;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaledProduct;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;

@Named @ViewScoped @Getter @Setter
public class SaleCrudOnePage extends AbstractCrudOnePage<Sale> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private SaleBusiness saleBusiness;
	@Inject private PaymentBusiness paymentBusiness;
	@Inject private ProductBusiness productBusiness;
	@Inject private CustomerBusiness customerBusiness;
	
	private List<Product> products;
	private List<Customer> customers;

	private Product selectedProduct;
	private Customer selectedCustomer;
	
	private Boolean collectProduct=Boolean.FALSE,collectMoney=Boolean.TRUE,showQuantityColumn = Boolean.TRUE;
	
	private PaymentController paymentController;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		products = new ArrayList<Product>(productBusiness.findAll());
		customers = new ArrayList<Customer>(customerBusiness.findAll());
		paymentController = new PaymentController(paymentBusiness,new Payment(),Boolean.FALSE);
		paymentController.getPayment().setSale(identifiable);
		sell();
	}
	
	/**/
	
	@Override
	protected void create() {
		saleBusiness.create(identifiable, paymentController.getPayment());
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