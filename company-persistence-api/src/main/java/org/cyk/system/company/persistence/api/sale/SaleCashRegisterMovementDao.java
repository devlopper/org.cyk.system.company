package org.cyk.system.company.persistence.api.sale;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.root.persistence.api.AbstractCollectionItemDao;

@Deprecated
public interface SaleCashRegisterMovementDao extends AbstractCollectionItemDao<SaleCashRegisterMovement,SaleCashRegisterMovementCollection> {

	Collection<SaleCashRegisterMovement> readBySale(Sale sale);
	Long countBySale(Sale sale);
	
	BigDecimal sumAmountBySale(Sale sale);
	SaleCashRegisterMovement readByCashRegisterMovementCode(String code);

}
