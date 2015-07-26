package org.cyk.system.company.business.impl.product;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.SaleStockBusiness;
import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.company.model.product.SaleStockSearchCriteria;
import org.cyk.system.company.persistence.api.product.SaleStockDao;

public class SaleStockBusinessImpl extends AbstractSaleStockBusinessImpl<SaleStock, SaleStockDao,SaleStockSearchCriteria> implements SaleStockBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject
	public SaleStockBusinessImpl(SaleStockDao dao) {
		super(dao);
	}

}
