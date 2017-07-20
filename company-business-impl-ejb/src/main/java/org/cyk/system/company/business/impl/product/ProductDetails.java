package org.cyk.system.company.business.impl.product;

import java.io.Serializable;

import org.cyk.system.company.model.product.Product;

public class ProductDetails extends AbstractProductDetails<Product> implements Serializable {

	private static final long serialVersionUID = 5592297849872101371L;

	public ProductDetails(Product product) {
		super(product);
	}

}
