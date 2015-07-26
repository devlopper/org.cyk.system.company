package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.cyk.system.company.business.api.product.SaleStockBusiness;
import org.cyk.system.company.business.impl.product.SaleStockReportTableRow;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.company.model.product.SaleStockSearchCriteria;
import org.cyk.system.company.ui.web.primefaces.model.SaleStockInputQueryResultFormModel;
import org.cyk.system.company.ui.web.primefaces.model.SaleStockQueryFormModel;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.ui.api.command.CommandAdapter;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.api.model.table.Row;
import org.cyk.ui.web.api.WebNavigationManager;
import org.cyk.ui.web.primefaces.Commandable;
import org.cyk.ui.web.primefaces.page.AbstractBusinessQueryPage;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;

import lombok.Getter;
import lombok.Setter; 

@Named @ViewScoped @Getter @Setter
public class SaleStockListPage extends AbstractBusinessQueryPage<SaleStock, SaleStockQueryFormModel, SaleStockListPage.SaleStockQueryResultFormModel> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject protected SaleStockBusiness saleStockBusiness;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		table.setShowHeader(Boolean.FALSE);
		table.setShowFooter(Boolean.FALSE);
		((Commandable)table.getAddRowCommandable()).getButton().setRendered(Boolean.FALSE);
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
				@SuppressWarnings("unchecked")
				Sale sale = ((Row<SaleStockInputQueryResultFormModel>)parameter).getData().getIdentifiable().getSale();
				WebNavigationManager.getInstance().redirectTo(businessEntityInfos.getUiConsultViewId(), 
						new Object[]{webManager.getRequestParameterIdentifiable(),sale.getIdentifier().toString()});
			}
		});
		
		table.setShowToolBar(Boolean.TRUE);
		
		table.setShowEditColumn(Boolean.FALSE);
		table.setShowAddRemoveColumn(Boolean.FALSE);
		table.getPrintCommandable().setRendered(Boolean.TRUE);
	}
	
	@Override
	protected void rowAdded(Row<Object> row) {
		super.rowAdded(row);
		row.setOpenable(Boolean.TRUE);
	}
	
	@Override
	protected Boolean autoLoad() {
		return Boolean.TRUE;
	}
	
	@Override
	protected Class<SaleStock> __entityClass__() {
		return SaleStock.class;
	}
	
	@Override
	protected Class<SaleStockQueryFormModel> __queryClass__() {
		return SaleStockQueryFormModel.class;
	}

	@Override
	protected Class<SaleStockQueryResultFormModel> __resultClass__() {
		return SaleStockQueryResultFormModel.class;
	}

	@Override
	protected Collection<SaleStock> __query__() {
		SaleStockSearchCriteria criteria = searchCriteria();
		
		criteria.getReadConfig().setFirstResultIndex(queryFirst);
		criteria.getReadConfig().setMaximumResultCount(20l);
		/*table.getColumn("cost").setFooter(numberBusiness.format(saleBusiness.sumCostByCriteria(criteria)));
		if(!BalanceType.ZERO.equals(balanceType)){
			table.getColumn("balance").setFooter(numberBusiness.format(saleBusiness.sumBalanceByCriteria(criteria)));
		}*/
		table.getPrintCommandable().setParameter(RootBusinessLayer.getInstance().getParameterFromDate(),criteria.getFromDateSearchCriteria().getPreparedValue().getTime());
		table.getPrintCommandable().setParameter(RootBusinessLayer.getInstance().getParameterToDate(),criteria.getToDateSearchCriteria().getPreparedValue().getTime());
		//if(balanceType!=null)
		//	table.getPrintCommandable().setParameter(CompanyBusinessLayer.getInstance().getParameterBalanceType(),balanceType.name());
		return saleStockBusiness.findByCriteria(criteria);
	}
	
	@Override
	protected Long __count__() {
		SaleStockSearchCriteria criteria = searchCriteria();
		
		return saleStockBusiness.countByCriteria(criteria);
	}

	protected SaleStockSearchCriteria searchCriteria(){
		SaleStockSearchCriteria searchCriteria = new SaleStockSearchCriteria(form.getData().getFromDate(),form.getData().getToDate());
		return searchCriteria;
	}
	
	/**/
	
	@Getter @Setter
	public static class SaleStockQueryResultFormModel extends AbstractFormModel<SaleStock> implements Serializable {

		private static final long serialVersionUID = -3328823824725030136L;

		@IncludeInputs
		private SaleStockReportTableRow saleStockReportTableRow;
		
		@Override
		public void read() {
			super.read();
			saleStockReportTableRow = new SaleStockReportTableRow(identifiable);
		}
		
	}
	
}