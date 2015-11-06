package org.cyk.system.company.business.api.production;

import java.util.Collection;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.model.production.ResellerProduct;
import org.cyk.system.root.business.api.TypedBusiness;

public interface ResellerProductBusiness extends TypedBusiness<ResellerProduct> {

	Collection<ResellerProduct> findByReseller(Reseller reseller);

	Collection<ResellerProduct> findByProduct(Product product);
	
}
