package org.cyk.system.company.business.impl.production;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ProductionBusiness;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionInput;
import org.cyk.system.company.persistence.api.production.ProductionDao;
import org.cyk.system.company.persistence.api.production.ProductionInputDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class ProductionBusinessImpl extends AbstractTypedBusinessService<Production, ProductionDao> implements ProductionBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject private ProductionInputDao productionInputDao;
	
	@Inject
	public ProductionBusinessImpl(ProductionDao dao) {
		super(dao);
	}

	@Override
	public Production create(Production production) {
		production.setCreationDate(universalTimeCoordinated());
		//exceptionUtils().exception(production.getPeriod().getToDate().before(production.getCreationDate()), "baddates");
		super.create(production);
		for(ProductionInput input : production.getInputs()){
			input.setProduction(production);
			productionInputDao.create(input);
		}
		return production;
	}
	
	@Override
	public Production update(Production production) {
		for(ProductionInput input : production.getInputs())
			productionInputDao.update(input);
		return super.update(production);
	}
	
	@Override
	public Production delete(Production production) {
		for(ProductionInput input : productionInputDao.readByProduction(production))
			productionInputDao.delete(input);
		return super.delete(production);
	}
	
	@Override
	public void load(Production production) {
		super.load(production);
		production.setInputs(productionInputDao.readByProduction(production));
	}
	
}
