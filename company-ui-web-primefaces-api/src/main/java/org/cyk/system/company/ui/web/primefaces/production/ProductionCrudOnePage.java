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

import org.cyk.system.company.business.api.production.ProductionPlanModelBusiness;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionInput;
import org.cyk.system.company.model.production.ProductionPlanModel;
import org.cyk.system.company.model.production.ProductionPlanModelInput;
import org.cyk.system.company.model.production.ProductionPlanModelMetric;
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
	private ProductionPlanModelBusiness productionPlanModelBusiness;
	
	private List<ProductionPlanModel> productionPlanModels;	
	private ProductionPlanModel selectedProductionPlanModel;
	private List<ProductionPlanModelInput> inputs;
	private List<ProductionPlanModelMetric> metrics;
	private ProductionInput[][] productionInputs;
	
	//private ItemCollection<PersonDetails> personCollection = new ItemCollection<>("qwerty",PersonDetails.class);
	
	@Override
	protected void initialisation() {
		super.initialisation();
		productionPlanModels = new ArrayList<ProductionPlanModel>(productionPlanModelBusiness.findAll());
		if(!productionPlanModels.isEmpty())
			selectedProductionPlanModel = productionPlanModels.get(0);
		productionPlanModelBusiness.load(selectedProductionPlanModel);
		inputs = new ArrayList<ProductionPlanModelInput>(selectedProductionPlanModel.getInputs());
		metrics = new ArrayList<ProductionPlanModelMetric>(selectedProductionPlanModel.getMetrics());
		
		productionInputs = new ProductionInput[inputs.size()][metrics.size()];
		int i=0,j=0;
		for(ProductionPlanModelInput input : inputs){
			for(ProductionPlanModelMetric productionPlanModelMetric : metrics){
				ProductionInput productionInput = new ProductionInput(input);
				productionInput.getMetricValue().setMetric(productionPlanModelMetric.getMetric());
				identifiable.getInputs().add(productionInput);
				productionInputs[i][j++] = productionInput;
			}
			i++;
			j=0;
		}
		//personCollection.setLabel("Details de personnes");
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
	
	/*
	@Getter @Setter @AllArgsConstructor @NoArgsConstructor
	public static class PersonDetails {
		
		@Input @InputText
		private String names;
		
		@Input @InputText
		private String dateOfBirth;
		
	}
	*/
	
}