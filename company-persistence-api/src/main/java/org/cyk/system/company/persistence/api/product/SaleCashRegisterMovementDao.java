package org.cyk.system.company.persistence.api.product;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.root.persistence.api.TypedDao;

public interface SaleCashRegisterMovementDao extends TypedDao<SaleCashRegisterMovement> {

	Collection<SaleCashRegisterMovement> readBySale(Sale sale);
	Long countBySale(Sale sale);
	
	BigDecimal sumAmount(Sale sale);

}
