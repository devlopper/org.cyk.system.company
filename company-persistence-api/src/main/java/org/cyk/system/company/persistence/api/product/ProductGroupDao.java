package org.cyk.system.company.persistence.api.product;

import org.cyk.system.company.model.product.ProductGroup;
import org.cyk.system.company.model.product.ProductGroupType;
import org.cyk.system.root.persistence.api.pattern.tree.AbstractDataTreeDao;

public interface ProductGroupDao extends AbstractDataTreeDao<ProductGroup,ProductGroupType> {

}
