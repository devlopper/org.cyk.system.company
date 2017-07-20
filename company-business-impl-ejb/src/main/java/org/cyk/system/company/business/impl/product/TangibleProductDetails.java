package org.cyk.system.company.business.impl.product;

import java.io.Serializable;

import org.cyk.system.company.model.product.TangibleProduct;

public class TangibleProductDetails extends AbstractProductDetails<TangibleProduct> implements Serializable {

	private static final long serialVersionUID = 5592297849872101371L;

	public TangibleProductDetails(TangibleProduct product) {
		super(product);
	}

}
