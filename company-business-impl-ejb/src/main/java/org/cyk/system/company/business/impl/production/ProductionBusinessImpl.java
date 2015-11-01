package org.cyk.system.company.business.impl.production;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ProductionBusiness;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionValue;
import org.cyk.system.company.model.production.ProductionSpreadSheetSearchCriteria;
import org.cyk.system.company.persistence.api.production.ProductionDao;
import org.cyk.system.company.persistence.api.production.ProductionValueDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class ProductionBusinessImpl extends AbstractTypedBusinessService<Production, ProductionDao> implements ProductionBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject private ProductionValueDao productionInputDao;
	
	@Inject
	public ProductionBusinessImpl(ProductionDao dao) {
		super(dao);
	}

	@Override
	public Production create(Production production) {
		production.setCreationDate(universalTimeCoordinated());
		//exceptionUtils().exception(production.getPeriod().getToDate().before(production.getCreationDate()), "baddates");
		super.create(production);
		for(ProductionValue input : production.getCells()){
			input.setSpreadSheet(production);
			productionInputDao.create(input);
		}
		return production;
	}
	
	@Override
	public Production update(Production production) {
		for(ProductionValue input : production.getCells())
			productionInputDao.update(input);
		return super.update(production);
	}
	
	@Override
	public Production delete(Production production) {
		for(ProductionValue input : productionInputDao.readByProduction(production))
			productionInputDao.delete(input);
		return super.delete(production);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void load(Production production) {
		super.load(production);
		production.setCells(productionInputDao.readByProduction(production));
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<Production> findByCriteria(ProductionSpreadSheetSearchCriteria searchCriteria) {
		prepareFindByCriteria(searchCriteria);
		return dao.readByCriteria(searchCriteria);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Long countByCriteria(ProductionSpreadSheetSearchCriteria searchCriteria) {
		return dao.countByCriteria(searchCriteria);
	}
	
}
