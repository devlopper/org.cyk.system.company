package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;

import javax.faces.model.SelectItem;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.sale.AbstractSaleStockTangibleProductMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleStockTangibleProductMovementBusiness;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.sale.SaleStockReportTableRow;
import org.cyk.system.company.model.sale.AbstractSaleStockTangibleProductMovementSearchCriteria;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovement;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.search.DefaultQueryFormModel;
import org.cyk.ui.api.data.collector.form.ControlSet;
import org.cyk.ui.api.model.table.ColumnAdapter;
import org.cyk.ui.api.model.table.Row;
import org.cyk.ui.web.api.data.collector.control.WebInput;
import org.cyk.ui.web.primefaces.Commandable;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.page.AbstractBusinessQueryPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.annotation.user.interfaces.Sequence;
import org.cyk.utility.common.annotation.user.interfaces.Sequence.Direction;
import org.cyk.utility.common.annotation.user.interfaces.Text;
import org.cyk.utility.common.model.table.Dimension.DimensionType;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;

@Getter @Setter
public abstract class AbstractSaleStockListPage<SALE_STOCK extends SaleStockTangibleProductMovement,SEARCH_CRITERIA extends AbstractSaleStockTangibleProductMovementSearchCriteria> extends AbstractBusinessQueryPage<SALE_STOCK, AbstractSaleStockListPage.SaleStockQueryFormModel, SaleStockReportTableRow> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject protected SaleStockTangibleProductMovementBusiness saleStockBusiness;
	@Inject protected CompanyReportRepository companyReportRepository;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		form.getControlSetListeners().add(new ControlSetAdapter<SaleStockQueryFormModel>(){
			
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
		table.getColumnListeners().add(new ColumnAdapter(){
			@Override
			public Boolean isColumn(Field field) {
				return !ignoreField(field);
			}
		});
	
		table.setShowHeader(Boolean.FALSE);
		table.setShowFooter(Boolean.FALSE);
		rowAdapter.setOpenable(Boolean.FALSE);
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
		table.getPrintCommandable().setParameter(uiManager.getClassParameter(),uiManager.businessEntityInfos(SaleStockTangibleProductMovement.class).getIdentifier());
		table.getPrintCommandable().setParameter(CompanyReportRepository.getInstance().getParameterSaleDone(),Boolean.TRUE);
	}
	
	@Override
	protected AbstractIdentifiable __identifiable__(Object data) {
		return ((SaleStockReportTableRow)data).getSaleStock();
	}
		
	@Override
	protected DimensionType getRowType(Row<Object> row) {
		return ((SaleStockReportTableRow)row.getData()).getSaleStock()==null ? DimensionType.SUMMARY:super.getRowType(row);
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
	protected Class<SaleStockReportTableRow> __resultClass__() {
		return SaleStockReportTableRow.class;
	}
	
	protected abstract Class<SEARCH_CRITERIA> searchCriteriaClass();
	protected abstract AbstractSaleStockTangibleProductMovementBusiness<SALE_STOCK, SEARCH_CRITERIA> business();

	@Override
	protected Collection<SALE_STOCK> __query__() {
		SEARCH_CRITERIA criteria = searchCriteria();
		criteria.getReadConfig().setFirstResultIndex(queryFirst);
		criteria.getReadConfig().setMaximumResultCount(20l);
		table.getPrintCommandable().setParameter(RootBusinessLayer.getInstance().getParameterFromDate(),criteria.getFromDateSearchCriteria().getPreparedValue().getTime());
		table.getPrintCommandable().setParameter(RootBusinessLayer.getInstance().getParameterToDate(),criteria.getToDateSearchCriteria().getPreparedValue().getTime());
		__beforeFindByCriteria__(criteria);
		Collection<SALE_STOCK> collection = business().findByCriteria(criteria);
		__afterFindByCriteria__(criteria,collection);
		return collection;
	}

	protected void __beforeFindByCriteria__(SEARCH_CRITERIA criteria){}
	protected void __afterFindByCriteria__(SEARCH_CRITERIA criteria,Collection<SALE_STOCK> results){}
	
	@Override
	protected Long __count__() {
		SEARCH_CRITERIA criteria = searchCriteria();
		
		return business().countByCriteria(criteria);
	}

	protected SEARCH_CRITERIA searchCriteria(){
		SEARCH_CRITERIA criteria = newInstance(searchCriteriaClass());
		criteria.getFromDateSearchCriteria().setValue(form.getData().getFromDate());
		criteria.getToDateSearchCriteria().setValue(form.getData().getToDate());
		criteria.getIdentifierStringSearchCriteria().setValue(form.getData().getIdentifier());
		criteria.getExternalIdentifierStringSearchCriteria().setValue(form.getData().getExternalIdentifier());
		return criteria;
	}
	
	/**/
	@Getter @Setter
	public static class SaleStockQueryFormModel extends DefaultQueryFormModel implements Serializable {

		private static final long serialVersionUID = -3328823824725030136L;
		@Input(label=@Text(value="field.sale.stock.input.external.identifier")) @InputText 
		@Sequence(direction=Direction.AFTER,field=DefaultQueryFormModel.FIELD_IDENTIFIER)
		private String externalIdentifier;
		
	}
	
}