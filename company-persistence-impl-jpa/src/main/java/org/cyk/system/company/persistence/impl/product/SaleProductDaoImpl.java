package org.cyk.system.company.persistence.impl.product;

import java.util.Collection;

import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.company.persistence.api.product.SaleProductDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class SaleProductDaoImpl extends AbstractTypedDao<SaleProduct> implements SaleProductDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readBySale;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readBySale, _select().where("sale"));
	}
	
	@Override
	public Collection<SaleProduct> readBySale(Sale sale) {
		return namedQuery(readBySale).parameter("sale", sale).resultMany();
	}

}
