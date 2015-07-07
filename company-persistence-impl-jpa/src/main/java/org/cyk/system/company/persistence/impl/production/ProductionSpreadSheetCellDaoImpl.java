package org.cyk.system.company.persistence.impl.production;

import java.util.Collection;

import org.cyk.system.company.model.production.ProductionSpreadSheet;
import org.cyk.system.company.model.production.ProductionSpreadSheetCell;
import org.cyk.system.company.persistence.api.production.ProductionSpreadSheetCellDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class ProductionSpreadSheetCellDaoImpl extends AbstractTypedDao<ProductionSpreadSheetCell> implements ProductionSpreadSheetCellDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readByProduction;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByProduction, _select().where("spreadSheet"));
	}
	
	@Override
	public Collection<ProductionSpreadSheetCell> readByProductionSpreadSheet(ProductionSpreadSheet production) {
		return namedQuery(readByProduction).parameter("spreadSheet", production).resultMany();
	}

	
	
}
