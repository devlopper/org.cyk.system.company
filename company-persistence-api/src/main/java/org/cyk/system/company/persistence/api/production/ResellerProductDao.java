package org.cyk.system.company.persistence.api.production;

import java.util.Collection;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.model.production.ResellerProduct;
import org.cyk.system.root.persistence.api.TypedDao;

public interface ResellerProductDao extends TypedDao<ResellerProduct> {

	ResellerProduct readByResellerByProduct(Reseller reseller,Product product);
	Collection<ResellerProduct> readByReseller(Reseller reseller);
	Collection<ResellerProduct> readByProduct(Product product);

}
