package org.cyk.system.company.persistence.impl.product;

import java.util.Collection;

import org.cyk.system.company.model.product.ProductEmployee;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.persistence.api.product.ProductEmployeeDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class ProductEmployeeDaoImpl extends AbstractTypedDao<ProductEmployee> implements ProductEmployeeDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readBySalePerformers;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readBySalePerformers, "SELECT pe FROM ProductEmployee pe WHERE EXISTS "
				+ "(SELECT sale FROM Sale sale WHERE sale = :pSale AND pe MEMBER OF sale.performers)");
	}
	
	@Override
	public Collection<ProductEmployee> readPerformersBySale(Sale sale) {
		return namedQuery(readBySalePerformers).parameter("pSale", sale).resultMany();
	}

}
