package org.cyk.system.company.persistence.impl.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.model.sale.SaleProductInstance;
import org.cyk.system.company.persistence.api.sale.SaleProductInstanceDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class SaleProductInstanceDaoImpl extends AbstractTypedDao<SaleProductInstance> implements SaleProductInstanceDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readBySaleProduct;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readBySaleProduct, _select().where(SaleProductInstance.FIELD_SALE_PRODUCT));
	}
	
	@Override
	public Collection<SaleProductInstance> readBySaleProduct(SaleProduct saleProduct) {
		return namedQuery(readBySaleProduct).parameter(SaleProductInstance.FIELD_SALE_PRODUCT, saleProduct).resultMany();
	}

	

}
