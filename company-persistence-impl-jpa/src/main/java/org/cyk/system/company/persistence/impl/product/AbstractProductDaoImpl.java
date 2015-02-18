package org.cyk.system.company.persistence.impl.product;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.persistence.api.product.AbstractProductDao;
import org.cyk.system.root.persistence.impl.AbstractEnumerationDaoImpl;

public abstract class AbstractProductDaoImpl<PRODUCT extends Product> extends AbstractEnumerationDaoImpl<PRODUCT> implements AbstractProductDao<PRODUCT> {

	private static final long serialVersionUID = 6920278182318788380L;

}
