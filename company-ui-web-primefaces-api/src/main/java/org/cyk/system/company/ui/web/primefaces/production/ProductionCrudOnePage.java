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

import org.cyk.system.company.business.api.production.ProductionPlanBusiness;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionValue;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.ui.web.primefaces.model.ProductionController;
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
	private ProductionPlanBusiness productionPlanModelBusiness;
	
	private List<ProductionPlan> productionPlans;	
	private ProductionPlan selectedProductionPlan;
	private ProductionController productionController;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		productionPlans = new ArrayList<ProductionPlan>(productionPlanModelBusiness.findAll());
		if(!productionPlans.isEmpty())
			selectedProductionPlan = productionPlans.get(0);
		productionPlanModelBusiness.load(selectedProductionPlan);
		
		productionController = new ProductionController(identifiable, new ArrayList<ProductionPlanResource>(selectedProductionPlan.getRows()),
				new ArrayList<ProductionPlanMetric>(selectedProductionPlan.getColumns()), null);
		
		productionController.setEditable(form.getEditable());
		for(ProductionPlanResource input : productionController.getRows()){
			for(ProductionPlanMetric productionPlanModelMetric : productionController.getColumns())
				identifiable.getCells().add(new ProductionValue(input,productionPlanModelMetric));
			
		}
		productionController.setCells(new ArrayList<ProductionValue>(identifiable.getCells()));
		
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
		@Override
		public void write() {
			super.write();
			identifiable.getPeriod().setFromDate(date);
			identifiable.getPeriod().setToDate(identifiable.getPeriod().getFromDate());
		}
	}
	
}