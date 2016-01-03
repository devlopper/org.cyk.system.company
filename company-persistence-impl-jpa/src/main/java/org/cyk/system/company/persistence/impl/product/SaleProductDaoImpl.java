package org.cyk.system.company.persistence.impl.product;

import java.util.Collection;

import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.persistence.api.product.SaleProductDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class SaleProductDaoImpl extends AbstractTypedDao<SaleProduct> implements SaleProductDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readBySale,readBySales;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readBySale, _select().where("sale"));
		registerNamedQuery(readBySales, _select().whereIdentifierIn("sale"));
	}
	
	@Override
	public Collection<SaleProduct> readBySale(Sale sale) {
		return namedQuery(readBySale).parameter("sale", sale).resultMany();
	}

	@Override
	public Collection<SaleProduct> readBySales(Collection<Sale> sales) {
		return namedQuery(readBySales).parameterIdentifiers(sales).resultMany();
	}

}
