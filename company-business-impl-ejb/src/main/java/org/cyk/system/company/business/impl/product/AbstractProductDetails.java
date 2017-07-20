package org.cyk.system.company.business.impl.product;

import java.io.Serializable;

import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.system.root.model.AbstractIdentifiable;

public abstract class AbstractProductDetails<PRODUCT extends AbstractIdentifiable> extends AbstractOutputDetails<PRODUCT> implements Serializable {

	private static final long serialVersionUID = 5592297849872101371L;

	public AbstractProductDetails(PRODUCT product) {
		super(product);
	}

}
