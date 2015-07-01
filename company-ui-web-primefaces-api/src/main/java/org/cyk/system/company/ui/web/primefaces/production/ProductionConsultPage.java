package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.production.ProductionBusiness;
import org.cyk.system.company.business.api.production.ProductionPlanModelBusiness;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionInput;
import org.cyk.system.company.model.production.ProductionPlanModel;
import org.cyk.system.company.model.production.ProductionPlanModelInput;
import org.cyk.system.company.model.production.ProductionPlanModelMetric;
import org.cyk.system.company.ui.web.primefaces.model.ProductionSpreadSheetController;
import org.cyk.ui.web.primefaces.page.AbstractPrimefacesPage;

@Named @ViewScoped @Getter @Setter
public class ProductionConsultPage extends AbstractPrimefacesPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject
	private ProductionBusiness productionBusiness;
	@Inject
	private ProductionPlanModelBusiness productionPlanModelBusiness;
	
	private Integer selectedMonthIndex;
	private List<ProductionSpreadSheetController> productionSpreadSheetControllers;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		ProductionPlanModel productionPlanModel = productionPlanModelBusiness.findAll().iterator().next();
		productionPlanModelBusiness.load(productionPlanModel);
		productionSpreadSheetControllers = new ArrayList<ProductionSpreadSheetController>();
		for(Production production : productionBusiness.findAll()){
			productionBusiness.load(production);
			productionSpreadSheetControllers.add(new ProductionSpreadSheetController(production, 
					new ArrayList<ProductionPlanModelInput>(productionPlanModel.getRows()), 
					new ArrayList<ProductionPlanModelMetric>(productionPlanModel.getColumns()), 
					new ArrayList<ProductionInput>(production.getCells())));
		}
	}
	
	/**/
	
	

}