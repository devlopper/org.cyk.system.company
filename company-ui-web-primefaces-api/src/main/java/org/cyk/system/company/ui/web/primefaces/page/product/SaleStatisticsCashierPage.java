package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.company.ui.web.primefaces.model.SaleFormModel;
import org.cyk.system.company.ui.web.primefaces.model.SaleQueryFormModel;

@Named @ViewScoped @Getter @Setter
public class SaleStatisticsCashierPage extends AbstractSaleListPage<SaleQueryFormModel, SaleFormModel> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Override
	protected Class<SaleQueryFormModel> __queryClass__() {
		return SaleQueryFormModel.class;
	}

	@Override
	protected Class<SaleFormModel> __resultClass__() {
		return SaleFormModel.class;
	}

	@Override
	protected SaleSearchCriteria searchCriteria() {
		return new SaleSearchCriteria(form.getData().getFromDate(),form.getData().getToDate());
	}
	
}