package org.cyk.system.company.business.impl.product;

import java.io.Serializable;

import org.cyk.system.company.model.product.IntangibleProduct;

public class IntangibleProductDetails extends AbstractProductDetails<IntangibleProduct> implements Serializable {

	private static final long serialVersionUID = 5592297849872101371L;

	public IntangibleProductDetails(IntangibleProduct product) {
		super(product);
	}

}
