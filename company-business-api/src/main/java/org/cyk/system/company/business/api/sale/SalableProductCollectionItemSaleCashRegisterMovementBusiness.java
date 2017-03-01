package org.cyk.system.company.business.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.SalableProductCollectionItemSaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.root.business.api.TypedBusiness;

public interface SalableProductCollectionItemSaleCashRegisterMovementBusiness extends TypedBusiness<SalableProductCollectionItemSaleCashRegisterMovement> {

	Collection<SalableProductCollectionItemSaleCashRegisterMovement> findBySaleCashRegisterMovement(SaleCashRegisterMovement saleCashRegisterMovement);

	
}
