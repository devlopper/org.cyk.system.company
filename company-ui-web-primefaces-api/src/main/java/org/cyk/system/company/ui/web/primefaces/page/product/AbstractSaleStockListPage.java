package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.AbstractSaleStockBusiness;
import org.cyk.system.company.business.api.product.SaleStockBusiness;
import org.cyk.system.company.business.impl.product.SaleStockReportTableRow;
import org.cyk.system.company.model.product.AbstractSaleStockSearchCriteria;
import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.search.DefaultQueryFormModel;
import org.cyk.ui.api.command.CommandAdapter;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.api.model.table.Cell;
import org.cyk.ui.api.model.table.Column;
import org.cyk.ui.api.model.table.Row;
import org.cyk.ui.web.primefaces.Commandable;
import org.cyk.ui.web.primefaces.page.AbstractBusinessQueryPage;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.annotation.user.interfaces.Sequence;
import org.cyk.utility.common.annotation.user.interfaces.Sequence.Direction;
import org.cyk.utility.common.annotation.user.interfaces.Text;
import org.cyk.utility.common.model.table.TableAdapter;

import lombok.Getter;
import lombok.Setter; 

@Getter @Setter
public abstract class AbstractSaleStockListPage<SALE_STOCK extends SaleStock,SEARCH_CRITERIA extends AbstractSaleStockSearchCriteria> extends AbstractBusinessQueryPage<SALE_STOCK, AbstractSaleStockListPage.SaleStockQueryFormModel, AbstractSaleStockListPage.SaleStockQueryResultFormModel> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject protected SaleStockBusiness saleStockBusiness;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		table.getTableListeners().add(new TableAdapter<Row<Object>, Column, Object, String, Cell, String>(){
			@Override
			public Boolean ignore(Field field) {
				return ignoreField(field);
			}
		});
	}
	
	protected abstract Boolean ignoreField(Field field);
	
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
	protected void rowAdded(Row<Object> row) {
		super.rowAdded(row);
		row.setOpenable(Boolean.TRUE);
	}
	
	@Override
	protected Boolean autoLoad() {
		return Boolean.TRUE;
	}
	
	@Override
	protected Class<SaleStockQueryFormModel> __queryClass__() {
		return SaleStockQueryFormModel.class;
	}

	@Override
	protected Class<SaleStockQueryResultFormModel> __resultClass__() {
		return SaleStockQueryResultFormModel.class;
	}
	
	protected abstract Class<SEARCH_CRITERIA> searchCriteriaClass();
	protected abstract AbstractSaleStockBusiness<SALE_STOCK, SEARCH_CRITERIA> business();

	@Override
	protected Collection<SALE_STOCK> __query__() {
		SEARCH_CRITERIA criteria = searchCriteria();
		
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
		return business().findByCriteria(criteria);
	}
	
	@Override
	protected Long __count__() {
		SEARCH_CRITERIA criteria = searchCriteria();
		
		return business().countByCriteria(criteria);
	}

	protected SEARCH_CRITERIA searchCriteria(){
		SEARCH_CRITERIA searchCriteria = newInstance(searchCriteriaClass());
		searchCriteria.getFromDateSearchCriteria().setValue(form.getData().getFromDate());
		searchCriteria.getToDateSearchCriteria().setValue(form.getData().getToDate());
		searchCriteria.getIdentifierStringSearchCriteria().setValue(form.getData().getIdentifier());
		searchCriteria.getExternalIdentifierStringSearchCriteria().setValue(form.getData().getExternalIdentifier());
		return searchCriteria;
	}
	
	/**/
	@Getter @Setter
	public static class SaleStockQueryFormModel extends DefaultQueryFormModel implements Serializable {

		private static final long serialVersionUID = -3328823824725030136L;
		//saleStockInputExternalIdentifier
		@Input(label=@Text(value="field.sale.stock.input.external.identifier")) @InputText 
		@Sequence(direction=Direction.AFTER,field=DefaultQueryFormModel.FIELD_IDENTIFIER_NAME)
		private String externalIdentifier;
		
	}
	
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