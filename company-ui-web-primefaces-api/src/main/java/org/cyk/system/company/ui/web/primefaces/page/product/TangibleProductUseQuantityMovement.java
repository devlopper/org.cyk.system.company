package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.TangibleProduct;

@Getter @Setter
public class TangibleProductUseQuantityMovement implements Serializable {

	private static final long serialVersionUID = -4511323642950758121L;
	private TangibleProduct tangibleProduct;
	private BigDecimal tempUseQuantity,addUseQuantity=BigDecimal.ZERO;
	
	public TangibleProductUseQuantityMovement(TangibleProduct tangibleProduct) {
		super();
		this.tangibleProduct = tangibleProduct;
		tempUseQuantity = tangibleProduct.getUseQuantity();
	}
	
	public void reset(){
		tangibleProduct.setUseQuantity(tempUseQuantity);
	}
	
	public void apply(){
		tangibleProduct.setUseQuantity(tempUseQuantity.add(addUseQuantity));
	}
}
