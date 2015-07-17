package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.production.ProductionSpreadSheetBusiness;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.company.model.production.ProductionSpreadSheet;
import org.cyk.system.company.model.production.ProductionSpreadSheetSearchCriteria;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.company.ui.web.primefaces.model.ProductionSpreadSheetQueryFormModel;
import org.cyk.system.company.ui.web.primefaces.model.ProductionSpreadSheetQueryResultFormModel;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.web.primefaces.Commandable;
import org.cyk.ui.web.primefaces.page.AbstractBusinessQueryPage;

@Named @ViewScoped @Getter @Setter
public class ProductionSpreadSheetQueryPage extends AbstractBusinessQueryPage<ProductionSpreadSheet, ProductionSpreadSheetQueryFormModel, 
	ProductionSpreadSheetQueryResultFormModel> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private ProductionSpreadSheetBusiness productionBusiness;

	@Override
	protected void initialisation() {
		super.initialisation();
		table.setShowToolBar(Boolean.TRUE);
		((Commandable)table.getAddRowCommandable()).updateLabel(languageBusiness.findDoFunctionnalityText(TangibleProductStockMovement.class));
		table.getAddRowCommandable().setRendered(Boolean.TRUE);
		table.setShowOpenCommand(Boolean.FALSE);
		
	}
	
	@Override
	protected Collection<UICommandable> contextualCommandables() {
		return CompanyWebManager.getInstance().stockContextCommandables(getUserSession());
	}
		
	@Override
	protected Collection<ProductionSpreadSheet> __query__() {
		ProductionSpreadSheetSearchCriteria searchCriteria = searchCriteria();
		Collection<ProductionSpreadSheet> collection = productionBusiness.findByCriteria(searchCriteria);
		table.getPrintCommandable().setParameter(RootBusinessLayer.getInstance().getParameterFromDate(),searchCriteria.getFromDateSearchCriteria().getPreparedValue().getTime());
		table.getPrintCommandable().setParameter(RootBusinessLayer.getInstance().getParameterToDate(),searchCriteria.getToDateSearchCriteria().getPreparedValue().getTime());
		return collection;
	}
	
	@Override
	protected Long __count__() {
		return productionBusiness.countByCriteria(searchCriteria());
	}
	
	@Override
	protected Boolean autoLoad() {
		return Boolean.TRUE;
	}
	
	private ProductionSpreadSheetSearchCriteria searchCriteria() {
		return new ProductionSpreadSheetSearchCriteria(form.getData().getFromDate(),form.getData().getToDate());
	}

	@Override
	protected Class<ProductionSpreadSheet> __entityClass__() {
		return ProductionSpreadSheet.class;
	}

	@Override
	protected Class<ProductionSpreadSheetQueryFormModel> __queryClass__() {
		return ProductionSpreadSheetQueryFormModel.class;
	}

	@Override
	protected Class<ProductionSpreadSheetQueryResultFormModel> __resultClass__() {
		return ProductionSpreadSheetQueryResultFormModel.class;
	}
	
	
}