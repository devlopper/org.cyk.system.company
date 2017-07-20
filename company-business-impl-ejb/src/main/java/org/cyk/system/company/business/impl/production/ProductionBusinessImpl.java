package org.cyk.system.company.business.impl.production;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ProductionBusiness;
import org.cyk.system.company.business.api.production.ProductionPlanBusiness;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionValue;
import org.cyk.system.company.persistence.api.production.ProductionDao;
import org.cyk.system.company.persistence.api.production.ProductionPlanMetricDao;
import org.cyk.system.company.persistence.api.production.ProductionPlanResourceDao;
import org.cyk.system.company.persistence.api.production.ProductionValueDao;
import org.cyk.system.root.business.impl.spreadsheet.AbstractSpreadSheetBusinessImpl;
import org.cyk.system.root.model.spreadsheet.SpreadSheetSearchCriteria;

public class ProductionBusinessImpl extends AbstractSpreadSheetBusinessImpl<Production,ProductionPlan,ProductionPlanResource,ProductionPlanMetric,ProductionValue,BigDecimal,SpreadSheetSearchCriteria,ProductionDao> implements ProductionBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject private ProductionValueDao productionValueDao;
	@Inject private ProductionPlanResourceDao productionPlanResourceDao;
	@Inject private ProductionPlanMetricDao productionPlanMetricDao;
	
	@Inject
	public ProductionBusinessImpl(ProductionDao dao) {
		super(dao);
	}
	
	@Override
	public Production instanciateOne(ProductionPlan productionPlan) {
		Production production = new Production();
		production.setTemplate(productionPlan);
		inject(ProductionPlanBusiness.class).load(productionPlan);
		for(ProductionPlanMetric metric : productionPlan.getColumns())
			production.getColumns().add(metric);
		for(ProductionPlanResource resource : productionPlan.getRows()){
			production.getRows().add(resource);
			for(ProductionPlanMetric metric : production.getColumns())
				production.getCells().add(new ProductionValue(resource,metric,null));
		}
		return production;
	}

	@Override
	public Production create(Production production) {
		production.setCreationDate(universalTimeCoordinated());
		//exceptionUtils().exception(production.getPeriod().getToDate().before(production.getCreationDate()), "baddates");
		//System.out.println(production.getCells());
		super.create(production);
		for(ProductionValue input : production.getCells()){
			input.setSpreadSheet(production);
			//debug(input);
			productionValueDao.create(input);
		}
		return production;
	}
	
	@Override
	public Production update(Production production) {
		for(ProductionValue input : production.getCells())
			productionValueDao.update(input);
		return super.update(production);
	}
	
	@Override
	public Production delete(Production production) {
		for(ProductionValue input : productionValueDao.readBySpreadSheet(production))
			productionValueDao.delete(input);
		return super.delete(production);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void load(Production production) {
		super.load(production);
		production.setCells(productionValueDao.readBySpreadSheet(production));
	}
	
	@Override
	protected void __load__(Production production) {
		super.__load__(production);
		production.setRows(productionPlanResourceDao.readByTemplate(production.getTemplate()));
		production.setColumns(productionPlanMetricDao.readByTemplate(production.getTemplate()));
		production.setCells(productionValueDao.readBySpreadSheet(production));
	}

	@Override
	public Collection<Production> findByProductionPlan(ProductionPlan productionPlan) {
		return dao.readByProductionPlan(productionPlan);
	}
	
}
