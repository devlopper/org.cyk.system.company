package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.sale.CustomerSalableProduct;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public class CustomerSalableProductDetails extends AbstractOutputDetails<CustomerSalableProduct> implements Serializable {
	private static final long serialVersionUID = -1498269103849317057L;
	
	@Input @InputText
	private String customer,salableProduct,price;
	
	public CustomerSalableProductDetails(CustomerSalableProduct customerSalableProduct) {
		super(customerSalableProduct);
		customer = formatUsingBusiness(customerSalableProduct.getCustomer());
		salableProduct = formatUsingBusiness(customerSalableProduct.getSalableProduct());
		price = formatNumber(customerSalableProduct.getPrice());
	}
	
	/**/
	
	
}