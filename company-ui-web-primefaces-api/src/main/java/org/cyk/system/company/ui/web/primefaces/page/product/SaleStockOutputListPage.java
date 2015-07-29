package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.cyk.system.company.business.api.product.SaleStockOutputBusiness;
import org.cyk.system.company.business.impl.product.SaleStockReportTableRow;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.company.model.product.SaleStockOutputSearchCriteria;
import org.cyk.system.company.ui.web.primefaces.model.SaleQueryResultFormModel;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.search.DefaultQueryFormModel;
import org.cyk.ui.api.command.CommandAdapter;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.api.model.table.Cell;
import org.cyk.ui.api.model.table.Column;
import org.cyk.ui.api.model.table.Row;
import org.cyk.ui.web.api.WebNavigationManager;
import org.cyk.ui.web.primefaces.Commandable;
import org.cyk.ui.web.primefaces.page.AbstractBusinessQueryPage;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.model.table.TableAdapter;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class SaleStockOutputListPage extends AbstractBusinessQueryPage<SaleStockOutput, DefaultQueryFormModel, SaleStockOutputListPage.SaleStockOutputQueryResultFormModel> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject protected SaleStockOutputBusiness saleStockOutputBusiness;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		table.setShowHeader(Boolean.FALSE);
		table.setShowFooter(Boolean.TRUE);
		contentTitle=text("company.report.salestockoutput.cashregister.title");
		((Commandable)table.getAddRowCommandable()).getButton().setRendered(Boolean.FALSE);
		TableAdapter<Row<Object>, Column, Object, String, Cell, String> listener;
		listener = new TableAdapter<Row<Object>, Column, Object, String, Cell, String>(){
			@Override
			public Boolean ignore(Field field) {
				return SaleStockReportTableRow.cashRegisterFieldIgnored(field);
			}
		};
		table.getTableListeners().add(listener);
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
				Sale sale = ((Row<SaleQueryResultFormModel>)parameter).getData().getIdentifiable();
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
	protected Class<SaleStockOutput> __entityClass__() {
		return SaleStockOutput.class;
	}
	
	@Override
	protected Class<DefaultQueryFormModel> __queryClass__() {
		return DefaultQueryFormModel.class;
	}

	@Override
	protected Class<SaleStockOutputQueryResultFormModel> __resultClass__() {
		return SaleStockOutputQueryResultFormModel.class;
	}

	@Override
	protected Collection<SaleStockOutput> __query__() {
		SaleStockOutputSearchCriteria criteria = searchCriteria();
		
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
		return saleStockOutputBusiness.findByCriteria(criteria);
	}
	
	@Override
	protected Long __count__() {
		SaleStockOutputSearchCriteria criteria = searchCriteria();
		
		return saleStockOutputBusiness.countByCriteria(criteria);
	}

	protected SaleStockOutputSearchCriteria searchCriteria(){
		SaleStockOutputSearchCriteria searchCriteria = new SaleStockOutputSearchCriteria(form.getData().getFromDate(),form.getData().getToDate());
		
		return searchCriteria;
	}
	
	/**/
	
	@Getter @Setter
	public static class SaleStockOutputQueryResultFormModel extends AbstractFormModel<SaleStockOutput> implements Serializable {

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