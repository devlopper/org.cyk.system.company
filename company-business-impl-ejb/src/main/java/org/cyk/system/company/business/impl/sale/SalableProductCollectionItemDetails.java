package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SalableProductCollectionItemDetails extends AbstractOutputDetails<SalableProductCollectionItem> implements Serializable{
	private static final long serialVersionUID = -4741435164709063863L;
	
	@Input @InputText
	private String code,name,unitPrice,quantity,price,instances;
	
	public SalableProductCollectionItemDetails(SalableProductCollectionItem salableProductCollectionItem) {
		super(salableProductCollectionItem);
		/*
		this.code = saleProduct.getSalableProduct().getProduct().getCode();
		this.name = saleProduct.getSalableProduct().getProduct().getName();
		this.unitPrice = saleProduct.getSalableProduct().getPrice()==null?Constant.EMPTY_STRING:formatNumber(saleProduct.getSalableProduct().getPrice());
		this.quantity = formatNumber(saleProduct.getQuantity());
		this.price = formatNumber(saleProduct.getCost().getValue());
		*/
		
	}
	
	/**/
	
	
}