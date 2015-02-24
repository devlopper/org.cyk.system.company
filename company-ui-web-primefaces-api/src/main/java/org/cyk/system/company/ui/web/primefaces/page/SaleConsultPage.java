package org.cyk.system.company.ui.web.primefaces.page;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.Payment;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaledProduct;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.root.business.api.Crud;
import org.cyk.ui.api.UIProvider;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.UICommandable.Parameter;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Named @ViewScoped @Getter @Setter
public class SaleConsultPage extends AbstractConsultPage<Sale> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	//@Inject private SaleBusiness saleBusiness;
	private FormOneData<SaleDetails> saleDetails;
	private Table<ProductDetails> saledProductsTable;
	private Table<PaymentDetails> paymentsTable;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void initialisation() {
		super.initialisation();
		contentTitle += " - "+identifiable.getIdentificationNumber();
		
		saleDetails = (FormOneData<SaleDetails>) createFormOneData(new SaleDetails(identifiable), Crud.READ);
		configureDetailsForm(saleDetails);
		
		saledProductsTable = (Table<ProductDetails>) createTable(ProductDetails.class, null, null);
		configureDetailsTable(saledProductsTable, "model.entity.product");
		
		paymentsTable = (Table<PaymentDetails>) createTable(PaymentDetails.class, null, null);
		configureDetailsTable(paymentsTable, "model.entity.payment");
		
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		for(SaledProduct saledProduct : identifiable.getSaledProducts())
			saledProductsTable.addRow(new ProductDetails(saledProduct));
		for(Payment payment : identifiable.getPayments())
			paymentsTable.addRow(new PaymentDetails(payment));
		
		saledProductsTable.getColumn("price").setFooter(identifiable.getCost().toString());
		paymentsTable.getColumn("paid").setFooter(identifiable.getCost().subtract(identifiable.getBalance()).toString());
		
		
	}
	
	@Override
	protected Collection<UICommandable> contextualCommandables() {
		Integer balance = identifiable.getBalance().compareTo(BigDecimal.ZERO);
		if(balance!=0){
			UICommandable contextualMenu = UIProvider.getInstance().createCommandable("button", null);
			contextualMenu.setLabel(contentTitle); 
			
			Collection<Parameter> parameters = Arrays.asList(new UICommandable.Parameter(uiManager.getClassParameter(), uiManager.keyFromClass(Payment.class)),
					new UICommandable.Parameter(uiManager.getCrudParameter(), uiManager.getCrudCreateParameter())
			,new UICommandable.Parameter(uiManager.keyFromClass(Sale.class), identifiable.getIdentifier()));
			Collection<Parameter> p;
			if(balance>0){
				p = new ArrayList<>(parameters);
				p.add(new UICommandable.Parameter(CompanyWebManager.getInstance().getRequestParameterPaymentType(), 
						CompanyWebManager.getInstance().getRequestParameterPay() ));
				contextualMenu.addChild("command.pay", null, "paymentEditView", p);	
			}else{
				p = new ArrayList<>(parameters);
				p.add(new UICommandable.Parameter(CompanyWebManager.getInstance().getRequestParameterPaymentType(), 
						CompanyWebManager.getInstance().getRequestParameterPayback() ));
				contextualMenu.addChild("command.payback", null, "paymentEditView", p);	
			}
			return Arrays.asList(contextualMenu);
		}
		return null;
	}
	
	/**/
	
	@Getter @Setter
	private class SaleDetails implements Serializable {
		private static final long serialVersionUID = -1498269103849317057L;
		
		@Input @InputText
		private String identifier,cost,balance,customer,date;
		
		public SaleDetails(Sale sale) {
			this.identifier = sale.getIdentificationNumber();
			this.cost = sale.getCost().toString();
			this.balance = sale.getBalance().toString();
			this.customer = sale.getCustomer()==null?"":sale.getCustomer().getPerson().getNames();
			this.date = uiManager.formatDate(sale.getDate(),Boolean.TRUE);
		}
	}
	
	@Getter @Setter
	private class ProductDetails implements Serializable {
		private static final long serialVersionUID = -1498269103849317057L;
		
		@Input @InputText
		private String name,unitPrice,quantity,price;
		
		public ProductDetails(SaledProduct saledProduct) {
			this.name = saledProduct.getProduct().getCode()+" - "+saledProduct.getProduct().getName();
			this.unitPrice = saledProduct.getProduct().getPrice().toString();
			this.quantity = saledProduct.getQuantity().toString();
			this.price = saledProduct.getPrice().toString();
		}
	}
	
	@Getter @Setter
	private class PaymentDetails implements Serializable {
		private static final long serialVersionUID = -6341285110719947720L;
		
		@Input @InputText
		private String in,out,paid,date;
		
		public PaymentDetails(Payment payment) {
			this.in = payment.getAmountIn().toString();
			this.out = payment.getAmountOut().toString();
			this.paid = payment.getAmountPaid().toString();
			this.date = uiManager.formatDate(payment.getDate(), Boolean.TRUE);
		}
	}
	
	@Override
	public Boolean getShowContextualMenu() {
		return Boolean.TRUE;
	}
	
}