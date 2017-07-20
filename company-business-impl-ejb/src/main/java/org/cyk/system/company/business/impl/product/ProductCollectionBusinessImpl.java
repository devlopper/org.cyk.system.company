package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.ProductCollectionBusiness;
import org.cyk.system.company.model.product.ProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.persistence.api.product.ProductCollectionDao;

public class ProductCollectionBusinessImpl extends AbstractProductBusinessImpl<ProductCollection, ProductCollectionDao> implements ProductCollectionBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public ProductCollectionBusinessImpl(ProductCollectionDao dao) {
		super(dao);
	}

	@Override
	public Collection<ProductCollection> findAllWithProduct() {
		return dao.readAllWithProduct();
	}

	@Override
	protected Set<ProductCollection> products(Collection<SalableProductCollectionItem> saleProducts) {
		return null;
	}

	@Override
	protected void beforeUpdate(ProductCollection product, BigDecimal usedCount) {
		
	}
	
}
