package org.cyk.system.company.business.api.stock;

import java.util.Collection;
import java.util.List;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.model.stock.StockTangibleProductMovementSearchCriteria;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.api.TypedBusiness;
@Deprecated
public interface StockTangibleProductMovementBusiness extends TypedBusiness<StockTangibleProductMovement> {

	StockTangibleProductMovement instanciateOne(String[] arguments);
	List<StockTangibleProductMovement> instanciateMany(String[][] arguments);

	/**
	 * Update the stock
	 * @param sale
	 */
	void consume(Sale sale, Crud crud, Boolean first);
	
	Collection<StockTangibleProductMovement> findByCriteria(StockTangibleProductMovementSearchCriteria criteria);
	Long countByCriteria(StockTangibleProductMovementSearchCriteria criteria);

}
