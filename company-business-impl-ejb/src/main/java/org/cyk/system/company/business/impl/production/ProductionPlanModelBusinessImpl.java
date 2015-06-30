package org.cyk.system.company.business.impl.production;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ProductionPlanModelBusiness;
import org.cyk.system.company.model.production.ProductionPlanModel;
import org.cyk.system.company.model.production.ProductionPlanModelInput;
import org.cyk.system.company.model.production.ProductionPlanModelMetric;
import org.cyk.system.company.persistence.api.production.ProductionPlanModelDao;
import org.cyk.system.company.persistence.api.production.ProductionPlanModelInputDao;
import org.cyk.system.company.persistence.api.production.ProductionPlanModelMetricDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class ProductionPlanModelBusinessImpl extends AbstractTypedBusinessService<ProductionPlanModel, ProductionPlanModelDao> implements ProductionPlanModelBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject private ProductionPlanModelInputDao productionPlanModelInputDao;
	@Inject private ProductionPlanModelMetricDao productionPlanModelMetricDao;
	
	@Inject
	public ProductionPlanModelBusinessImpl(ProductionPlanModelDao dao) {
		super(dao);
	}

	@Override
	public ProductionPlanModel create(ProductionPlanModel productionPlanModel) {
		super.create(productionPlanModel);
		for(ProductionPlanModelInput input : productionPlanModel.getInputs()){
			input.setProductionPlanModel(productionPlanModel);
			productionPlanModelInputDao.create(input);
		}
		for(ProductionPlanModelMetric metric : productionPlanModel.getMetrics()){
			metric.setProductionPlanModel(productionPlanModel);
			productionPlanModelMetricDao.create(metric);
		}
		return productionPlanModel;
	}
	
	@Override
	public ProductionPlanModel update(ProductionPlanModel productionPlanModel) {
		for(ProductionPlanModelInput input : productionPlanModel.getInputs())
			productionPlanModelInputDao.update(input);
		for(ProductionPlanModelMetric metric : productionPlanModel.getMetrics())
			productionPlanModelMetricDao.update(metric);
		return super.update(productionPlanModel);
	}
	
	@Override
	public ProductionPlanModel delete(ProductionPlanModel productionPlanModel) {
		for(ProductionPlanModelInput input : productionPlanModelInputDao.readByProductionPlanModel(productionPlanModel))
			productionPlanModelInputDao.delete(input);
		for(ProductionPlanModelMetric metric : productionPlanModelMetricDao.readByProductionPlanModel(productionPlanModel))
			productionPlanModelMetricDao.delete(metric);
		return super.delete(productionPlanModel);
	}
	
	@Override
	public void load(ProductionPlanModel productionPlanModel) {
		super.load(productionPlanModel);
		productionPlanModel.setInputs(productionPlanModelInputDao.readByProductionPlanModel(productionPlanModel));
		productionPlanModel.setMetrics(productionPlanModelMetricDao.readByProductionPlanModel(productionPlanModel));
	}
	
}
