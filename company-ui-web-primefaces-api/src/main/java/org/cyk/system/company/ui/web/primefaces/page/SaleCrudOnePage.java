package org.cyk.system.company.ui.web.primefaces.page;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.product.CustomerBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaledProductBusiness;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaledProduct;
import org.cyk.system.company.model.product.Payment;
import org.cyk.system.company.model.product.Product;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;

@Named @ViewScoped @Getter @Setter
public class SaleCrudOnePage extends AbstractCrudOnePage<Sale> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private SaleBusiness saleBusiness;
	@Inject private SaledProductBusiness saledProductBusiness;
	@Inject private ProductBusiness productBusiness;
	@Inject private CustomerBusiness customerBusiness;
	
	private List<Product> products;
	private List<Customer> customers;

	private Product selectedProduct;
	private Customer selectedCustomer;
	private Payment payment;
	
	private Boolean collectProduct=Boolean.FALSE,collectMoney=Boolean.TRUE,showQuantityColumn = Boolean.TRUE;
	private BigDecimal /*totalPrice = BigDecimal.ZERO,amountPaid=BigDecimal.ZERO,*/
			amountToHand=BigDecimal.ZERO/*,amountHanded=BigDecimal.ZERO*/,balance=BigDecimal.ZERO;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		products = new ArrayList<Product>(productBusiness.findAll());
		customers = new ArrayList<Customer>(customerBusiness.findAll());
		payment = new Payment();
		sell();
	}
	
	/**/
	
	@Override
	public void serve(UICommand command, Object parameter) {
		//super.serve(command, parameter);
		System.out.println("SaleCrudOnePage.serve()");
	}
	
	public void addProduct(){
		identifiable.getSaledProducts().add(new SaledProduct(identifiable,selectedProduct,1));
		updateTotalPrice();
	}
	
	public void deleteProduct(Integer index){
		identifiable.getSaledProducts().remove(index.intValue());
		updateTotalPrice();
		//collectProduct = identifiable.getCost().equals(BigDecimal.ZERO);
	}
	
	public void cash(){
		collectProduct = Boolean.FALSE;
		collectMoney = Boolean.TRUE;
	}
	
	public void sell(){
		collectProduct = Boolean.TRUE;
		collectMoney = Boolean.FALSE;
	}
	
	public void updateProductPrice(Integer index){
		saledProductBusiness.process(identifiable.getSaledProducts().get(index));
	}
	
	public void updateTotalPrice(){
		saleBusiness.process(identifiable);
	}
	
	public void productQuantityChanged(Integer index){
		updateProductPrice(index);
		updateTotalPrice();
	}
	
	public void amountPaidChanged(){
		amountToHand = payment.getAmountPaid().subtract(identifiable.getCost());
	}
	
	public void amountHandedChanged(){
		balance = payment.getAmountHanded().subtract(amountToHand);
	}
	
	
	
}