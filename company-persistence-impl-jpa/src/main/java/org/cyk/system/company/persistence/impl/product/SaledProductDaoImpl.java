package org.cyk.system.company.persistence.impl.product;

import java.util.Collection;

import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaledProduct;
import org.cyk.system.company.persistence.api.product.SaledProductDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class SaledProductDaoImpl extends AbstractTypedDao<SaledProduct> implements SaledProductDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readBySale;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readBySale, _select().where("sale"));
	}
	
	@Override
	public Collection<SaledProduct> readBySale(Sale sale) {
		return namedQuery(readBySale).parameter("sale", sale).resultMany();
	}

}
