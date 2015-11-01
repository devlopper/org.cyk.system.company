package org.cyk.system.company.business.impl.production;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ProductionSpreadSheetCellBusiness;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionSpreadSheetCell;
import org.cyk.system.company.persistence.api.production.ProductionSpreadSheetCellDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class ProductionSpreadSheetCellBusinessImpl extends AbstractTypedBusinessService<ProductionSpreadSheetCell, ProductionSpreadSheetCellDao> implements ProductionSpreadSheetCellBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject
	public ProductionSpreadSheetCellBusinessImpl(ProductionSpreadSheetCellDao dao) {
		super(dao);
	}

	@Override
	public Collection<ProductionSpreadSheetCell> findByProductionSpreadSheet(Production production) {
		return dao.readByProductionSpreadSheet(production);
	}

	
	
}
