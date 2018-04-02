package org.cyk.system.company.persistence.impl.product;

import org.cyk.system.company.model.product.ProductGroup;
import org.cyk.system.company.model.product.ProductGroupType;
import org.cyk.system.company.persistence.api.product.ProductGroupDao;
import org.cyk.system.root.persistence.impl.pattern.tree.AbstractDataTreeDaoImpl;

public class ProductGroupDaoImpl extends AbstractDataTreeDaoImpl<ProductGroup,ProductGroupType> implements ProductGroupDao {

	private static final long serialVersionUID = 6920278182318788380L;

}
