package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.cyk.system.company.business.api.product.AbstractSaleStockBusiness;
import org.cyk.system.company.business.api.product.SaleStockBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.product.SaleStockReportTableRow;
import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.company.model.product.SaleStockSearchCriteria;
import org.cyk.ui.api.command.CommandAdapter;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.web.primefaces.Commandable;

import lombok.Getter;
import lombok.Setter; 

@Named @ViewScoped @Getter @Setter
public class SaleStockListPage extends AbstractSaleStockListPage<SaleStock, SaleStockSearchCriteria> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject protected SaleStockBusiness saleStockBusiness;
	
	private String type;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		type = requestParameter(CompanyBusinessLayer.getInstance().getParameterSaleStockReportType());
		if(CompanyBusinessLayer.getInstance().getParameterSaleStockReportInventory().equals(type))
			contentTitle=text("company.report.salestockoutput.inventory.title");
		else if(CompanyBusinessLayer.getInstance().getParameterSaleStockReportCustomer().equals(type))
			contentTitle=text("company.report.salestockoutput.customer.title");
		
	}
	
	@Override
	protected Boolean ignoreField(Field field) {
		
		if(CompanyBusinessLayer.getInstance().getParameterSaleStockReportInventory().equals(type))
			return SaleStockReportTableRow.inventoryFieldIgnored(field);
		else if(CompanyBusinessLayer.getInstance().getParameterSaleStockReportCustomer().equals(type))
			return SaleStockReportTableRow.customerFieldIgnored(field);
		return Boolean.TRUE;
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();

		table.setShowEditColumn(Boolean.FALSE);		
		
		((Commandable)table.getOpenRowCommandable()).getButton().setRendered(Boolean.TRUE);
		((Commandable)table.getRemoveRowCommandable()).getButton().setRendered(Boolean.FALSE);
		
		table.getOpenRowCommandable().getCommand().getCommandListeners().add(new CommandAdapter(){
			private static final long serialVersionUID = 1120566504648934547L;
			@Override
			public void serve(UICommand command, Object parameter) {
				/*@SuppressWarnings("unchecked")
				Sale sale = ((Row<SaleStockQueryResultFormModel>)parameter).getData().getIdentifiable().getSale();
				WebNavigationManager.getInstance().redirectTo(businessEntityInfos.getUiConsultViewId(), 
						new Object[]{webManager.getRequestParameterIdentifiable(),sale.getIdentifier().toString()});*/
			}
		});
		
		table.setShowToolBar(Boolean.TRUE);
		table.setShowEditColumn(Boolean.FALSE);
		table.setShowAddRemoveColumn(Boolean.FALSE);
		table.getPrintCommandable().setRendered(Boolean.TRUE);
	}
			
	@Override
	protected Class<SaleStock> __entityClass__() {
		return SaleStock.class;
	}

	@Override
	protected Class<SaleStockSearchCriteria> searchCriteriaClass() {
		return SaleStockSearchCriteria.class;
	}

	@Override
	protected AbstractSaleStockBusiness<SaleStock, SaleStockSearchCriteria> business() {
		return saleStockBusiness;
	}
	
	
	

}