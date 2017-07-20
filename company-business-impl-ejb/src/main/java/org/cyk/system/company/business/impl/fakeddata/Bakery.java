package org.cyk.system.company.business.impl.fakeddata;

import java.io.Serializable;

import org.cyk.utility.common.cdi.AbstractBean;

public class Bakery extends AbstractBean implements Serializable {

	private static final long serialVersionUID = -4180196113740218243L;

	/*
    private void bakeryFakeTransaction(){
    	ProductionSpreadSheetTemplate productionPlanModel = new ProductionSpreadSheetTemplate("PPM1","Production de pain");
    	productionPlanModel.setTimeDivisionType(RootBusinessLayer.getInstance().getTimeDivisionTypeDay());
    	productionPlanModel.getRows().add(new ProductionSpreadSheetTemplateRow(fakeTangibleProduct("F", "Farine", null, null)));
    	productionPlanModel.getRows().add(new ProductionSpreadSheetTemplateRow(fakeTangibleProduct("L", "Levure", null, null)));
    	productionPlanModel.getRows().add(new ProductionSpreadSheetTemplateRow(fakeTangibleProduct("A", "Ameliorant", null, null)));
    	productionPlanModel.getRows().add(new ProductionSpreadSheetTemplateRow(fakeTangibleProduct("B", "Bois", null, null)));
    	productionPlanModel.getRows().add(new ProductionSpreadSheetTemplateRow(fakeTangibleProduct("G", "Gaz", null, null)));
    	productionPlanModel.getRows().add(new ProductionSpreadSheetTemplateRow(fakeTangibleProduct("C", "Carburant", null, null)));
    	productionPlanModel.getRows().add(new ProductionSpreadSheetTemplateRow(fakeTangibleProduct("GA", "Glace alimentaire", null, null)));
    	productionPlanModel.getRows().add(new ProductionSpreadSheetTemplateRow(fakeTangibleProduct("PV", "Pain vendable", null, null)));
    	productionPlanModel.getRows().add(new ProductionSpreadSheetTemplateRow(fakeTangibleProduct("PNV", "Pain non vendu", null, null)));
    	
    	productionPlanModel.getColumns().add(new ProductionSpreadSheetTemplateColumn(inputName("PLANNED", "Planned Quantity")));
    	productionPlanModel.getColumns().add(new ProductionSpreadSheetTemplateColumn(inputName("FACTORED", "Factored Quantity")));
    	productionPlanModel.getColumns().add(new ProductionSpreadSheetTemplateColumn(inputName("UNUSED", "Unused Quantity")));
    	
    	productionPlanModelBusiness.create(productionPlanModel);
    	
		productionPlanModelBusiness.load(productionPlanModel);
    	ProductionSpreadSheet production = new ProductionSpreadSheet();
    	production.getPeriod().setFromDate(new Date());
    	production.getPeriod().setToDate(production.getPeriod().getFromDate());
    	for(ProductionSpreadSheetTemplateRow input : productionPlanModel.getRows()){
			for(ProductionSpreadSheetTemplateColumn productionPlanModelMetric : productionPlanModel.getColumns()){
				ProductionSpreadSheetCell productionInput = new ProductionSpreadSheetCell(input,productionPlanModelMetric);
				//productionInput.getMetricValue().setInput(productionPlanModelMetric.getInputName());
				production.getCells().add(productionInput);
				productionInput.setValue(new BigDecimal(RandomDataProvider.getInstance().randomInt(0, 9999)));
			}
		}
    	productionBusiness.create(production);
    }
    */
	
}
