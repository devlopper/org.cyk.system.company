package org.cyk.system.company.persistence.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.SalableProductCollectionItemSaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.root.persistence.api.TypedDao;

public interface SalableProductCollectionItemSaleCashRegisterMovementDao extends TypedDao<SalableProductCollectionItemSaleCashRegisterMovement> {
	
	Collection<SalableProductCollectionItemSaleCashRegisterMovement> readBySalableProductCollectionItem(SalableProductCollectionItem salableProductCollectionItem);
	Collection<SalableProductCollectionItemSaleCashRegisterMovement> readBySaleCashRegisterMovement(SaleCashRegisterMovement saleCashRegisterMovement);
		
}
