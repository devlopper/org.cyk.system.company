package org.cyk.system.company.business.impl.production;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.production.ProductionSpreadSheetTemplateBusiness;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionSpreadSheetTemplateColumn;
import org.cyk.system.company.persistence.api.production.ProductionSpreadSheetTemplateDao;
import org.cyk.system.company.persistence.api.production.ProductionSpreadSheetTemplateRowDao;
import org.cyk.system.company.persistence.api.production.ProductionSpreadSheetTemplateColumnDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class ProductionSpreadSheetTemplateBusinessImpl extends AbstractTypedBusinessService<ProductionPlan, ProductionSpreadSheetTemplateDao> implements ProductionSpreadSheetTemplateBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject private ProductionSpreadSheetTemplateRowDao productionPlanModelInputDao;
	@Inject private ProductionSpreadSheetTemplateColumnDao productionPlanModelMetricDao;
	
	@Inject
	public ProductionSpreadSheetTemplateBusinessImpl(ProductionSpreadSheetTemplateDao dao) {
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
		for(ProductionSpreadSheetTemplateColumn column : productionPlanModel.getColumns()){
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
		for(ProductionSpreadSheetTemplateColumn column : productionPlanModel.getColumns())
			productionPlanModelMetricDao.update(column);
		return super.update(productionPlanModel);
	}
	
	@Override
	public ProductionPlan delete(ProductionPlan productionPlanModel) {
		for(ProductionPlanResource row : productionPlanModelInputDao.readByProductionSpreadSheetTemplate(productionPlanModel))
			productionPlanModelInputDao.delete(row);
		for(ProductionSpreadSheetTemplateColumn column : productionPlanModelMetricDao.readByProductionSpreadSheetTemplate(productionPlanModel))
			productionPlanModelMetricDao.delete(column);
		return super.delete(productionPlanModel);
	}
	
	@Override
	public void load(ProductionPlan productionPlanModel) {
		super.load(productionPlanModel);
		productionPlanModel.setRows(productionPlanModelInputDao.readByProductionSpreadSheetTemplate(productionPlanModel));
		productionPlanModel.setColumns(productionPlanModelMetricDao.readByProductionSpreadSheetTemplate(productionPlanModel));
	}
	
}
