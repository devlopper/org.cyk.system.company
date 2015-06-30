package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.product.TangibleProductStockMovementBusiness;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.company.model.product.TangibleProductStockMovementSearchCriteria;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.company.ui.web.primefaces.model.TangibleProductStockMovementQueryFormModel;
import org.cyk.system.company.ui.web.primefaces.model.TangibleProductStockMovementQueryResultFormModel;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.web.primefaces.Commandable;
import org.cyk.ui.web.primefaces.page.AbstractBusinessQueryPage;

@Named @ViewScoped @Getter @Setter
public class TangibleProductStockMovementQueryPage extends AbstractBusinessQueryPage<TangibleProductStockMovement, TangibleProductStockMovementQueryFormModel, 
	TangibleProductStockMovementQueryResultFormModel> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private TangibleProductStockMovementBusiness tangibleProductStockMovementBusiness;

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
	protected Collection<TangibleProductStockMovement> __query__() {
		TangibleProductStockMovementSearchCriteria searchCriteria = searchCriteria();
		Collection<TangibleProductStockMovement> collection = tangibleProductStockMovementBusiness.findByCriteria(searchCriteria);
		table.getPrintCommandable().setParameter(RootBusinessLayer.getInstance().getParameterFromDate(),searchCriteria.getFromDateSearchCriteria().getPreparedValue().getTime());
		table.getPrintCommandable().setParameter(RootBusinessLayer.getInstance().getParameterToDate(),searchCriteria.getToDateSearchCriteria().getPreparedValue().getTime());
		return collection;
	}
	
	@Override
	protected Long __count__() {
		return tangibleProductStockMovementBusiness.countByCriteria(searchCriteria());
	}
	
	@Override
	protected Boolean autoLoad() {
		return Boolean.TRUE;
	}
	
	private TangibleProductStockMovementSearchCriteria searchCriteria() {
		return new TangibleProductStockMovementSearchCriteria(form.getData().getFromDate(),form.getData().getToDate());
	}

	@Override
	protected Class<TangibleProductStockMovement> __entityClass__() {
		return TangibleProductStockMovement.class;
	}

	@Override
	protected Class<TangibleProductStockMovementQueryFormModel> __queryClass__() {
		return TangibleProductStockMovementQueryFormModel.class;
	}

	@Override
	protected Class<TangibleProductStockMovementQueryResultFormModel> __resultClass__() {
		return TangibleProductStockMovementQueryResultFormModel.class;
	}
	
	
}