package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import org.cyk.system.company.business.impl.product.ProductDetails;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SalableProductDetails extends AbstractOutputDetails<SalableProduct> implements Serializable {
	private static final long serialVersionUID = -1498269103849317057L;
	
	@IncludeInputs private ProductDetails product;
	@Input @InputText private String price;
	
	public SalableProductDetails(SalableProduct salableProduct) {
		super(salableProduct);
		product = new ProductDetails(salableProduct.getProduct());
		price = formatNumber(salableProduct.getPrice());
	}
	
	/**/
	
	public static final String FIELD_PRODUCT = "product";
	public static final String FIELD_PRICE = "price";
}