package org.cyk.system.company.business.impl.production;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ProductionSpreadSheetBusiness;
import org.cyk.system.company.model.production.ProductionSpreadSheet;
import org.cyk.system.company.model.production.ProductionSpreadSheetCell;
import org.cyk.system.company.model.production.ProductionSpreadSheetSearchCriteria;
import org.cyk.system.company.persistence.api.production.ProductionSpreadSheetDao;
import org.cyk.system.company.persistence.api.production.ProductionSpreadSheetCellDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class ProductionSpreadSheetBusinessImpl extends AbstractTypedBusinessService<ProductionSpreadSheet, ProductionSpreadSheetDao> implements ProductionSpreadSheetBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject private ProductionSpreadSheetCellDao productionInputDao;
	
	@Inject
	public ProductionSpreadSheetBusinessImpl(ProductionSpreadSheetDao dao) {
		super(dao);
	}

	@Override
	public ProductionSpreadSheet create(ProductionSpreadSheet production) {
		production.setCreationDate(universalTimeCoordinated());
		//exceptionUtils().exception(production.getPeriod().getToDate().before(production.getCreationDate()), "baddates");
		super.create(production);
		for(ProductionSpreadSheetCell input : production.getCells()){
			input.setSpreadSheet(production);
			productionInputDao.create(input);
		}
		return production;
	}
	
	@Override
	public ProductionSpreadSheet update(ProductionSpreadSheet production) {
		for(ProductionSpreadSheetCell input : production.getCells())
			productionInputDao.update(input);
		return super.update(production);
	}
	
	@Override
	public ProductionSpreadSheet delete(ProductionSpreadSheet production) {
		for(ProductionSpreadSheetCell input : productionInputDao.readByProductionSpreadSheet(production))
			productionInputDao.delete(input);
		return super.delete(production);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void load(ProductionSpreadSheet production) {
		super.load(production);
		production.setCells(productionInputDao.readByProductionSpreadSheet(production));
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<ProductionSpreadSheet> findByCriteria(ProductionSpreadSheetSearchCriteria searchCriteria) {
		getPersistenceService().getDataReadConfig().set(searchCriteria.getReadConfig());
		return dao.readByCriteria(searchCriteria);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Long countByCriteria(ProductionSpreadSheetSearchCriteria searchCriteria) {
		return dao.countByCriteria(searchCriteria);
	}
	
}
