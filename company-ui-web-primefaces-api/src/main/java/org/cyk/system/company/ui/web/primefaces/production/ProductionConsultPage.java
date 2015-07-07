package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.production.ProductionSpreadSheetBusiness;
import org.cyk.system.company.business.api.production.ProductionSpreadSheetTemplateBusiness;
import org.cyk.system.company.model.production.ProductionSpreadSheet;
import org.cyk.system.company.model.production.ProductionSpreadSheetCell;
import org.cyk.system.company.model.production.ProductionSpreadSheetTemplate;
import org.cyk.system.company.model.production.ProductionSpreadSheetTemplateRow;
import org.cyk.system.company.model.production.ProductionSpreadSheetTemplateColumn;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.company.ui.web.primefaces.model.ProductionSpreadSheetController;
import org.cyk.system.company.ui.web.primefaces.model.ProductionSpreadSheetQueryFormModel;
import org.cyk.system.root.business.api.Crud;
import org.cyk.ui.api.command.CommandAdapter;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.web.primefaces.Tree;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.AbstractPrimefacesPage;

@Named @ViewScoped @Getter @Setter
public class ProductionConsultPage extends AbstractPrimefacesPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private ProductionSpreadSheetBusiness productionBusiness;
	@Inject private ProductionSpreadSheetTemplateBusiness productionPlanModelBusiness;
	
	private Tree tree = new Tree();
	private Integer selectedMonthIndex;
	private List<ProductionSpreadSheetController> productionSpreadSheetControllers;
	private FormOneData<ProductionSpreadSheetQueryFormModel> form;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		form = (FormOneData<ProductionSpreadSheetQueryFormModel>) createFormOneData(new ProductionSpreadSheetQueryFormModel(), Crud.CREATE);
		form.setDynamic(Boolean.TRUE);
		form.getSubmitCommandable().getCommand().getCommandListeners().add(new CommandAdapter(){
			private static final long serialVersionUID = -4051260187568895766L;
			@Override
			public void serve(UICommand command, Object parameter) {
				
			}
		});
		contentTitle = text("company.production.consult.page.content.title");
		ProductionSpreadSheetTemplate productionPlanModel = productionPlanModelBusiness.findAll().iterator().next();
		productionPlanModelBusiness.load(productionPlanModel);
		productionSpreadSheetControllers = new ArrayList<ProductionSpreadSheetController>();
		for(ProductionSpreadSheet production : productionBusiness.findAll()){
			productionBusiness.load(production);
			productionSpreadSheetControllers.add(new ProductionSpreadSheetController(production, 
					new ArrayList<ProductionSpreadSheetTemplateRow>(productionPlanModel.getRows()), 
					new ArrayList<ProductionSpreadSheetTemplateColumn>(productionPlanModel.getColumns()), 
					new ArrayList<ProductionSpreadSheetCell>(production.getCells())));
		}
		
	}
	
	@Override
	protected Collection<UICommandable> contextualCommandables() {
		return CompanyWebManager.getInstance().productionContextCommandables(getUserSession());
	}
	
	
	
}