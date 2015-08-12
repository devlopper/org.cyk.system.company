package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;

import javax.faces.model.SelectItem;
import javax.inject.Inject;

import org.cyk.system.company.business.api.product.AbstractSaleStockBusiness;
import org.cyk.system.company.business.api.product.SaleStockBusiness;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.product.SaleStockReportTableRow;
import org.cyk.system.company.model.product.AbstractSaleStockSearchCriteria;
import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.search.DefaultQueryFormModel;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.api.data.collector.form.ControlSet;
import org.cyk.ui.api.model.table.Cell;
import org.cyk.ui.api.model.table.Column;
import org.cyk.ui.api.model.table.Row;
import org.cyk.ui.web.api.data.collector.control.WebInput;
import org.cyk.ui.web.primefaces.Commandable;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.page.AbstractBusinessQueryPage;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.annotation.user.interfaces.Sequence;
import org.cyk.utility.common.annotation.user.interfaces.Sequence.Direction;
import org.cyk.utility.common.annotation.user.interfaces.Text;
import org.cyk.utility.common.model.table.TableAdapter;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;

import lombok.Getter;
import lombok.Setter; 

@Getter @Setter
public abstract class AbstractSaleStockListPage<SALE_STOCK extends SaleStock,SEARCH_CRITERIA extends AbstractSaleStockSearchCriteria> extends AbstractBusinessQueryPage<SALE_STOCK, AbstractSaleStockListPage.SaleStockQueryFormModel, AbstractSaleStockListPage.SaleStockQueryResultFormModel> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject protected SaleStockBusiness saleStockBusiness;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		form.setControlSetListener(new ControlSetAdapter<SaleStockQueryFormModel>(){
			
			@Override
			public Boolean build(Field field) {
				return !field.getName().equals("identifier");
			}
			
			@Override
			public Boolean showFieldLabel(
					ControlSet<SaleStockQueryFormModel, DynaFormModel, DynaFormRow, DynaFormLabel, DynaFormControl, SelectItem> controlSet,
					Field field) {
				return Boolean.FALSE;
			}
			
			@Override
			public Boolean canCreateRow(
					ControlSet<SaleStockQueryFormModel, DynaFormModel, DynaFormRow, DynaFormLabel, DynaFormControl, SelectItem> controlSet,
					Object object) {
				return Boolean.FALSE;
			}
			
			@Override
			public void input(
					ControlSet<SaleStockQueryFormModel, DynaFormModel, DynaFormRow, DynaFormLabel, DynaFormControl, SelectItem> controlSet,
					org.cyk.ui.api.data.collector.control.Input<?, DynaFormModel, DynaFormRow, DynaFormLabel, DynaFormControl, SelectItem> input) {
				super.input(controlSet, input);
				((WebInput<?, ?, ?, ?>)input).getCss().addClass("cyk-ui-form-salestock-search-inputfield");
			}
		}); 
		table.getTableListeners().add(new TableAdapter<Row<Object>, Column, Object, String, Cell, String>(){
			@Override
			public Boolean ignore(Field field) {
				return ignoreField(field);
			}
		});
	
		table.setShowHeader(Boolean.FALSE);
		table.setShowFooter(Boolean.FALSE);
	}
	
	protected abstract Boolean ignoreField(Field field);
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();

		((Commandable)table.getAddRowCommandable()).getButton().setRendered(Boolean.FALSE);
		((Commandable)table.getOpenRowCommandable()).getButton().setRendered(Boolean.FALSE);
		((Commandable)table.getRemoveRowCommandable()).getButton().setRendered(Boolean.FALSE);
		
		table.setShowToolBar(Boolean.TRUE);
		table.setShowEditColumn(Boolean.FALSE);
		table.setShowAddRemoveColumn(Boolean.FALSE);
		table.getPrintCommandable().setRendered(Boolean.TRUE);
		table.getPrintCommandable().setParameter(uiManager.getClassParameter(),uiManager.businessEntityInfos(SaleStock.class).getIdentifier());
		table.getPrintCommandable().setParameter(CompanyReportRepository.getInstance().getParameterSaleDone(),Boolean.TRUE);
	}
	
	@Override
	protected void rowAdded(Row<Object> row) {
		super.rowAdded(row);
		row.setOpenable(Boolean.FALSE);
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
		table.getPrintCommandable().setParameter(RootBusinessLayer.getInstance().getParameterFromDate(),criteria.getFromDateSearchCriteria().getPreparedValue().getTime());
		table.getPrintCommandable().setParameter(RootBusinessLayer.getInstance().getParameterToDate(),criteria.getToDateSearchCriteria().getPreparedValue().getTime());
		__beforeFindByCriteria__(criteria);
		return business().findByCriteria(criteria);
	}
	
	protected void __beforeFindByCriteria__(SEARCH_CRITERIA criteria){}
	
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
		@Sequence(direction=Direction.AFTER,field=DefaultQueryFormModel.FIELD_IDENTIFIER)
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