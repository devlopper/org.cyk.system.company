package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public class SalableProductDetails extends AbstractOutputDetails<SalableProduct> implements Serializable {
	private static final long serialVersionUID = -1498269103849317057L;
	
	@Input @InputText
	private String code,name,price;
	
	public SalableProductDetails(SalableProduct saleProduct) {
		super(saleProduct);
		code = saleProduct.getProduct().getCode();
		name = formatUsingBusiness(saleProduct.getProduct());
		price = formatNumber(saleProduct.getPrice());
	}
	
	/**/
	
	
}