package org.cyk.system.company.persistence.api.sale;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.root.persistence.api.TypedDao;

public interface SaleCashRegisterMovementDao extends TypedDao<SaleCashRegisterMovement> {

	Collection<SaleCashRegisterMovement> readBySale(Sale sale);
	Long countBySale(Sale sale);
	
	BigDecimal sumAmount(Sale sale);

}
