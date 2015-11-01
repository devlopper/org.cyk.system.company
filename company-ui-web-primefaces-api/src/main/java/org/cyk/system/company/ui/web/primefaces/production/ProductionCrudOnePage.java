package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.business.api.production.ProductionSpreadSheetTemplateBusiness;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionSpreadSheetCell;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionSpreadSheetTemplateColumn;
import org.cyk.system.company.ui.web.primefaces.model.ProductionSpreadSheetController;
import org.cyk.system.root.business.api.BusinessEntityInfos;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputCalendar;
import org.cyk.utility.common.annotation.user.interfaces.InputCalendar.Format;

@Named @ViewScoped @Getter @Setter
public class ProductionCrudOnePage extends AbstractCrudOnePage<Production> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject
	private ProductionSpreadSheetTemplateBusiness productionPlanModelBusiness;
	
	private List<ProductionPlan> productionPlanModels;	
	private ProductionPlan selectedProductionPlanModel;
	private ProductionSpreadSheetController productionSpreadSheetController;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		productionPlanModels = new ArrayList<ProductionPlan>(productionPlanModelBusiness.findAll());
		if(!productionPlanModels.isEmpty())
			selectedProductionPlanModel = productionPlanModels.get(0);
		productionPlanModelBusiness.load(selectedProductionPlanModel);
		
		productionSpreadSheetController = new ProductionSpreadSheetController(identifiable, new ArrayList<ProductionPlanResource>(selectedProductionPlanModel.getRows()),
				new ArrayList<ProductionSpreadSheetTemplateColumn>(selectedProductionPlanModel.getColumns()), null);
		
		productionSpreadSheetController.setEditable(form.getEditable());
		for(ProductionPlanResource input : productionSpreadSheetController.getRows()){
			for(ProductionSpreadSheetTemplateColumn productionPlanModelMetric : productionSpreadSheetController.getColumns())
				identifiable.getCells().add(new ProductionSpreadSheetCell(input,productionPlanModelMetric));
			
		}
		productionSpreadSheetController.setCells(new ArrayList<ProductionSpreadSheetCell>(identifiable.getCells()));
		
	}
	
	@Override
	protected void create() {
		identifiable.getPeriod().setFromDate(((FormModel)form.getData()).getDate());
		identifiable.getPeriod().setToDate(identifiable.getPeriod().getFromDate());
		super.create();
	}
	
	@Override
	protected BusinessEntityInfos fetchBusinessEntityInfos() {
		return uiManager.businessEntityInfos(Production.class);
	}
	
	@Override
	protected Class<? extends AbstractFormModel<?>> __formModelClass__() {
		return FormModel.class;
	}
			
	/**/
	@Getter @Setter @AllArgsConstructor @NoArgsConstructor
	public static class FormModel extends AbstractFormModel<Production> implements Serializable {

		private static final long serialVersionUID = -731657715703646576L;
		
		@Input @InputCalendar(format=Format.DATE_LONG)
		private Date date;
		
	}
	
}