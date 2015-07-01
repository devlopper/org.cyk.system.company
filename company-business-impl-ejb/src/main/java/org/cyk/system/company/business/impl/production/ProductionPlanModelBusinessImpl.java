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
		Integer index = 0;
		for(ProductionPlanModelInput row : productionPlanModel.getRows()){
			row.setTemplate(productionPlanModel);
			row.setIndex(index++);
			productionPlanModelInputDao.create(row);
		}
		index = 0;
		for(ProductionPlanModelMetric column : productionPlanModel.getColumns()){
			column.setTemplate(productionPlanModel);
			column.setIndex(index++);
			productionPlanModelMetricDao.create(column);
		}
		return productionPlanModel;
	}
	
	@Override
	public ProductionPlanModel update(ProductionPlanModel productionPlanModel) {
		for(ProductionPlanModelInput row : productionPlanModel.getRows())
			productionPlanModelInputDao.update(row);
		for(ProductionPlanModelMetric column : productionPlanModel.getColumns())
			productionPlanModelMetricDao.update(column);
		return super.update(productionPlanModel);
	}
	
	@Override
	public ProductionPlanModel delete(ProductionPlanModel productionPlanModel) {
		for(ProductionPlanModelInput row : productionPlanModelInputDao.readByProductionPlanModel(productionPlanModel))
			productionPlanModelInputDao.delete(row);
		for(ProductionPlanModelMetric column : productionPlanModelMetricDao.readByProductionPlanModel(productionPlanModel))
			productionPlanModelMetricDao.delete(column);
		return super.delete(productionPlanModel);
	}
	
	@Override
	public void load(ProductionPlanModel productionPlanModel) {
		super.load(productionPlanModel);
		productionPlanModel.setRows(productionPlanModelInputDao.readByProductionPlanModel(productionPlanModel));
		productionPlanModel.setColumns(productionPlanModelMetricDao.readByProductionPlanModel(productionPlanModel));
	}
	
}
