package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.sale.SaleSearchCriteria;
import org.cyk.system.company.ui.web.primefaces.model.SaleQueryResultFormModel;
import org.cyk.system.company.ui.web.primefaces.model.SaleQueryFormModel;

@Named @ViewScoped @Getter @Setter
public class SaleListPage extends AbstractSaleListPage<SaleQueryFormModel, SaleQueryResultFormModel> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Override
	protected Class<SaleQueryFormModel> __queryClass__() {
		return SaleQueryFormModel.class;
	}

	@Override
	protected Class<SaleQueryResultFormModel> __resultClass__() {
		return SaleQueryResultFormModel.class;
	}

	@Override
	protected SaleSearchCriteria searchCriteria() {
		return new SaleSearchCriteria(form.getData().getFromDate(),form.getData().getToDate());
	}
	
}