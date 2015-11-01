package org.cyk.system.company.business.impl.production;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ProductionPlanBusiness;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.persistence.api.production.ProductionPlanDao;
import org.cyk.system.company.persistence.api.production.ProductionPlanResourceDao;
import org.cyk.system.company.persistence.api.production.ProductionPlanMetricDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class ProductionPlanBusinessImpl extends AbstractTypedBusinessService<ProductionPlan, ProductionPlanDao> implements ProductionPlanBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject private ProductionPlanResourceDao productionPlanModelInputDao;
	@Inject private ProductionPlanMetricDao productionPlanModelMetricDao;
	
	@Inject
	public ProductionPlanBusinessImpl(ProductionPlanDao dao) {
		super(dao);
	}

	@Override
	public ProductionPlan create(ProductionPlan productionPlanModel) {
		super.create(productionPlanModel);
		Integer index = 0;
		for(ProductionPlanResource row : productionPlanModel.getRows()){
			row.setTemplate(productionPlanModel);
			row.setIndex(index++);
			productionPlanModelInputDao.create(row);
		}
		index = 0;
		for(ProductionPlanMetric column : productionPlanModel.getColumns()){
			column.setTemplate(productionPlanModel);
			column.setIndex(index++);
			productionPlanModelMetricDao.create(column);
		}
		return productionPlanModel;
	}
	
	@Override
	public ProductionPlan update(ProductionPlan productionPlanModel) {
		for(ProductionPlanResource row : productionPlanModel.getRows())
			productionPlanModelInputDao.update(row);
		for(ProductionPlanMetric column : productionPlanModel.getColumns())
			productionPlanModelMetricDao.update(column);
		return super.update(productionPlanModel);
	}
	
	@Override
	public ProductionPlan delete(ProductionPlan productionPlanModel) {
		for(ProductionPlanResource row : productionPlanModelInputDao.readByProductionPlan(productionPlanModel))
			productionPlanModelInputDao.delete(row);
		for(ProductionPlanMetric column : productionPlanModelMetricDao.readByProductionPlan(productionPlanModel))
			productionPlanModelMetricDao.delete(column);
		return super.delete(productionPlanModel);
	}
	
	@Override
	public void load(ProductionPlan productionPlanModel) {
		super.load(productionPlanModel);
		productionPlanModel.setRows(productionPlanModelInputDao.readByProductionPlan(productionPlanModel));
		productionPlanModel.setColumns(productionPlanModelMetricDao.readByProductionPlan(productionPlanModel));
	}
	
}
