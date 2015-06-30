package org.cyk.system.company.ui.web.primefaces.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

@Getter @Setter
public class SaleStockInputFormModel extends AbstractFormModel<SaleStockInput> implements Serializable {

	private static final long serialVersionUID = -7403076234556118486L;
	
	@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull
	private Customer customer;
	
	@Input @InputNumber @NotNull //@Size(min=1,max=Integer.MAX_VALUE)
	private BigDecimal quantity;
	
	@Input @InputNumber @NotNull //@Size(min=0,max=Integer.MAX_VALUE)
	private BigDecimal price;
	
	@Override
	public void read() {
		super.read();
		this.customer = identifiable.getSale().getCustomer();
		this.price = identifiable.getSale().getCost();
		this.quantity = identifiable.getTangibleProductStockMovement().getQuantity();
	}
	
	@Override
	public void write() {
		super.write();
		identifiable.getSale().setCustomer(customer);
		identifiable.getTangibleProductStockMovement().setQuantity(quantity);
		SaleProduct saleProduct = identifiable.getSale().getSaleProducts().iterator().next();
		saleProduct.setPrice(price);
	}
}