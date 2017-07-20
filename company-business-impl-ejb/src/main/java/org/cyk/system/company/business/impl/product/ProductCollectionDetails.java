package org.cyk.system.company.business.impl.product;

import java.io.Serializable;

import org.cyk.system.company.model.product.ProductCollection;

public class ProductCollectionDetails extends AbstractProductDetails<ProductCollection> implements Serializable {

	private static final long serialVersionUID = 5592297849872101371L;

	public ProductCollectionDetails(ProductCollection product) {
		super(product);
	}

}
