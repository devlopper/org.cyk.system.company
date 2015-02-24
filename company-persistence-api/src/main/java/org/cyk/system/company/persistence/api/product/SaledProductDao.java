package org.cyk.system.company.persistence.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaledProduct;
import org.cyk.system.root.persistence.api.TypedDao;

public interface SaledProductDao extends TypedDao<SaledProduct> {

	Collection<SaledProduct> readBySale(Sale sale);

}
