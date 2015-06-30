package org.cyk.system.company.persistence.api.product;

import java.util.Collection;

import org.cyk.system.company.model.product.TangibleProductInventory;
import org.cyk.system.company.model.product.TangibleProductInventoryDetail;
import org.cyk.system.root.persistence.api.TypedDao;

public interface TangibleProductInventoryDetailDao extends TypedDao<TangibleProductInventoryDetail> {

	Collection<TangibleProductInventoryDetail> readByTangibleProductInventory(TangibleProductInventory tangibleProductInventory);
    
}
