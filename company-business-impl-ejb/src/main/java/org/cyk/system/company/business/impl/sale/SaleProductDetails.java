package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleProductDetails extends AbstractOutputDetails<SaleProduct> implements Serializable {
	private static final long serialVersionUID = -1498269103849317057L;
	
	@Input @InputText
	private String name,unitPrice,quantity,price;
	
	public SaleProductDetails(SaleProduct saleProduct) {
		super(saleProduct);
		//this.name = saleProduct.getProduct().getCode()+" - "+saleProduct.getProduct().getName();
		//this.unitPrice = saleProduct.getProduct().getPrice()==null?"":numberBusiness.format(saleProduct.getProduct().getPrice());
		this.quantity = numberBusiness.format(saleProduct.getQuantity());
		//this.price = numberBusiness.format(saleProduct.getPrice());
	}
}