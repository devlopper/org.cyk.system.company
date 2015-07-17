package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.SaleStockInputSearchCriteria;

@Named @ViewScoped @Getter @Setter
public class SaleStockInStockListPage extends AbstractSaleStockInputListPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Override
	protected void initialisation() {
		super.initialisation();
		contentTitle = text("company.command.salestock.instock");
	}
	
	@Override
	protected SaleStockInputSearchCriteria searchCriteria() {
		SaleStockInputSearchCriteria searchCriteria = super.searchCriteria();
		searchCriteria.setMinimumRemainingGoodsCount(BigDecimal.ONE);
		return searchCriteria;
	}
	
}