package org.cyk.system.company.ui.web.primefaces.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.cyk.system.company.business.api.stock.StockTangibleProductMovementBusiness;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.model.stock.StockTangibleProductMovementSearchCriteria;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.primefaces.Commandable;
import org.cyk.ui.web.primefaces.page.AbstractBusinessQueryPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputCalendar;
import org.cyk.utility.common.annotation.user.interfaces.InputCalendar.Format;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class TangibleProductStockMovementQueryPage extends AbstractBusinessQueryPage<StockTangibleProductMovement, TangibleProductStockMovementQueryPage.TangibleProductStockMovementQueryFormModel, 
	TangibleProductStockMovementQueryPage.TangibleProductStockMovementQueryResultFormModel> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private StockTangibleProductMovementBusiness tangibleProductStockMovementBusiness;

	@Override
	protected void initialisation() {
		super.initialisation();
		table.setShowToolBar(Boolean.TRUE);
		//((Commandable)table.getAddRowCommandable()).updateLabel(languageBusiness.findDoFunctionnalityText(TangibleProductStockMovement.class));
		table.getAddRowCommandable().setRendered(Boolean.TRUE);
		table.setShowOpenCommand(Boolean.FALSE);
		
	}
	
	@Override
	protected Collection<UICommandable> contextualCommandables() {
		return CompanyWebManager.getInstance().stockContextCommandables(getUserSession());
	}
		
	@Override
	protected Collection<StockTangibleProductMovement> __query__() {
		StockTangibleProductMovementSearchCriteria searchCriteria = searchCriteria();
		Collection<StockTangibleProductMovement> collection = tangibleProductStockMovementBusiness.findByCriteria(searchCriteria);
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
	
	private StockTangibleProductMovementSearchCriteria searchCriteria() {
		return null;//new StockTangibleProductMovementSearchCriteria(form.getData().getFromDate(),form.getData().getToDate());
	}

	@Override
	protected Class<StockTangibleProductMovement> __entityClass__() {
		return StockTangibleProductMovement.class;
	}

	@Override
	protected Class<TangibleProductStockMovementQueryFormModel> __queryClass__() {
		return TangibleProductStockMovementQueryFormModel.class;
	}

	@Override
	protected Class<TangibleProductStockMovementQueryResultFormModel> __resultClass__() {
		return TangibleProductStockMovementQueryResultFormModel.class;
	}
	
	/**/
	
	@Getter @Setter
	public class TangibleProductStockMovementQueryFormModel implements Serializable {

		private static final long serialVersionUID = -3328823824725030136L;

		@Input @InputCalendar
		private Date fromDate;
		
		@Input @InputCalendar
		private Date toDate;
		
	}
	
	@Getter @Setter
	public static class TangibleProductStockMovementQueryResultFormModel extends AbstractFormModel<StockTangibleProductMovement> implements Serializable {

		private static final long serialVersionUID = -3328823824725030136L;

		@Input @InputChoice @InputOneChoice @InputOneCombo
		private TangibleProduct tangibleProduct;
		
		@Input @InputCalendar(format=Format.DATETIME_SHORT) private Date date;
		@Input @InputNumber private BigDecimal quantity;
		
		//@Input @InputTextarea
		private String comments;
		
	}


	
}