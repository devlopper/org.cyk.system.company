package org.cyk.system.company.persistence.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.ProductCollection;

public interface ProductCollectionDao extends AbstractProductDao<ProductCollection> {

	Collection<ProductCollection> readAllWithProduct();
   
}
