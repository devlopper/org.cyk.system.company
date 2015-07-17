package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.product.SaleStockInputBusiness;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockInputSearchCriteria;
import org.cyk.system.company.ui.web.primefaces.model.SaleStockInputQueryResultFormModel;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.search.DefaultQueryFormModel;
import org.cyk.ui.api.command.CommandAdapter;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.api.model.table.Row;
import org.cyk.ui.web.api.WebNavigationManager;
import org.cyk.ui.web.primefaces.Commandable;
import org.cyk.ui.web.primefaces.page.AbstractBusinessQueryPage;

@Getter @Setter
public abstract class AbstractSaleStockInputListPage extends AbstractBusinessQueryPage<SaleStockInput, DefaultQueryFormModel, SaleStockInputQueryResultFormModel> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject protected SaleStockInputBusiness saleStockInputBusiness;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		table.setShowHeader(Boolean.FALSE);
		table.setShowFooter(Boolean.FALSE);
		((Commandable)table.getAddRowCommandable()).getButton().setRendered(Boolean.FALSE);
		/*
		String minimumRemainingGoodsCount = requestParameter(CompanyWebManager.getInstance().getRequestParameterQuantity());
		if(StringUtils.isNotBlank(minimumRemainingGoodsCount))
			this.minimumRemainingGoodsCount = new BigDecimal(minimumRemainingGoodsCount);
		else
			this.minimumRemainingGoodsCount = BigDecimal.ZERO;
			*/
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
	protected Class<SaleStockInput> __entityClass__() {
		return SaleStockInput.class;
	}
	
	@Override
	protected Class<DefaultQueryFormModel> __queryClass__() {
		return DefaultQueryFormModel.class;
	}

	@Override
	protected Class<SaleStockInputQueryResultFormModel> __resultClass__() {
		return SaleStockInputQueryResultFormModel.class;
	}

	@Override
	protected Collection<SaleStockInput> __query__() {
		SaleStockInputSearchCriteria criteria = searchCriteria();
		
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
		return saleStockInputBusiness.findByCriteria(criteria);
	}
	
	@Override
	protected Long __count__() {
		SaleStockInputSearchCriteria criteria = searchCriteria();
		
		return saleStockInputBusiness.countByCriteria(criteria);
	}

	protected SaleStockInputSearchCriteria searchCriteria(){
		SaleStockInputSearchCriteria searchCriteria = new SaleStockInputSearchCriteria(form.getData().getFromDate(),form.getData().getToDate());
		searchCriteria.getIdentifierStringSearchCriteria().setValue(form.getData().getIdentifier());
		return searchCriteria;
	}
	
}