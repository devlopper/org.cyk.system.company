package org.cyk.system.company.business.impl.production;

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ProductionPlanBusiness;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionUnit;
import org.cyk.system.company.persistence.api.production.ProductionPlanDao;
import org.cyk.system.company.persistence.api.production.ProductionPlanMetricDao;
import org.cyk.system.company.persistence.api.production.ProductionPlanResourceDao;
import org.cyk.system.root.business.impl.spreadsheet.AbstractSpreadSheetTemplateBusinessImpl;

public class ProductionPlanBusinessImpl extends AbstractSpreadSheetTemplateBusinessImpl<ProductionPlan,ProductionPlanResource,ProductionPlanMetric,ProductionPlanDao> implements ProductionPlanBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject private ProductionPlanResourceDao productionPlanResourceDao;
	@Inject private ProductionPlanMetricDao productionPlanMetricDao;
	
	@Inject
	public ProductionPlanBusinessImpl(ProductionPlanDao dao) {
		super(dao);
	}

	@Override
	public ProductionPlan create(ProductionPlan productionPlan) {
		super.create(productionPlan);
		Integer index = 0;
		for(ProductionPlanResource row : productionPlan.getRows()){
			row.setTemplate(productionPlan);
			row.setIndex(index++);
			productionPlanResourceDao.create(row);
		}
		index = 0;
		for(ProductionPlanMetric column : productionPlan.getColumns()){
			column.setTemplate(productionPlan);
			column.setIndex(index++);
			productionPlanMetricDao.create(column);
		}
		return productionPlan;
	}
	
	@Override
	public ProductionPlan update(ProductionPlan productionPlan) {
		for(ProductionPlanResource row : productionPlan.getRows())
			productionPlanResourceDao.update(row);
		for(ProductionPlanMetric column : productionPlan.getColumns())
			productionPlanMetricDao.update(column);
		return super.update(productionPlan);
	}
	
	@Override
	public ProductionPlan delete(ProductionPlan productionPlanModel) {
		for(ProductionPlanResource row : productionPlanResourceDao.readByTemplate(productionPlanModel))
			productionPlanResourceDao.delete(row);
		for(ProductionPlanMetric column : productionPlanMetricDao.readByTemplate(productionPlanModel))
			productionPlanMetricDao.delete(column);
		return super.delete(productionPlanModel);
	}
	
	@Override
	public Collection<ProductionPlan> findByProductionUnit(ProductionUnit productionUnit) {
		return dao.readByProductionUnit(productionUnit);
	}
	
}
