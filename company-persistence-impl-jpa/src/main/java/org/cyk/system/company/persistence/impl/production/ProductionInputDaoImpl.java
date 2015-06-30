package org.cyk.system.company.persistence.impl.production;

import java.util.Collection;

import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionInput;
import org.cyk.system.company.persistence.api.production.ProductionInputDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class ProductionInputDaoImpl extends AbstractTypedDao<ProductionInput> implements ProductionInputDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readByProduction;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByProduction, _select().where("production"));
	}
	
	@Override
	public Collection<ProductionInput> readByProduction(Production production) {
		return namedQuery(readByProduction).parameter("production", production).resultMany();
	}

	
	
}
