package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SaleStockBusiness;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovement;
import org.cyk.system.company.model.sale.SaleStockSearchCriteria;
import org.cyk.system.company.model.sale.SaleStocksDetails;
import org.cyk.system.company.persistence.api.sale.SaleStockTangibleProductMovementDao;

public class SaleStockBusinessImpl extends AbstractSaleStockBusinessImpl<SaleStockTangibleProductMovement, SaleStockTangibleProductMovementDao,SaleStockSearchCriteria> implements SaleStockBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject
	public SaleStockBusinessImpl(SaleStockTangibleProductMovementDao dao) {
		super(dao);
	}

	@Override
	public SaleStocksDetails computeByCriteria(SaleStockSearchCriteria criteria) {
		return dao.computeByCriteria(criteria);
	}

	

}
