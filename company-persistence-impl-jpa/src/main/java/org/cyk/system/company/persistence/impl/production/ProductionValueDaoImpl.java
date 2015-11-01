package org.cyk.system.company.persistence.impl.production;

import java.util.Collection;

import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionValue;
import org.cyk.system.company.persistence.api.production.ProductionValueDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class ProductionValueDaoImpl extends AbstractTypedDao<ProductionValue> implements ProductionValueDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readByProduction;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByProduction, _select().where("spreadSheet"));
	}
	
	@Override
	public Collection<ProductionValue> readByProduction(Production production) {
		return namedQuery(readByProduction).parameter("spreadSheet", production).resultMany();
	}

	
	
}
