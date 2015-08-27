package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.model.payment.BalanceType;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.company.ui.web.primefaces.model.SaleQueryResultFormModel;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.ui.api.command.CommandAdapter;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.api.model.table.ColumnAdapter;
import org.cyk.ui.api.model.table.Row;
import org.cyk.ui.web.api.WebNavigationManager;
import org.cyk.ui.web.primefaces.Commandable;
import org.cyk.ui.web.primefaces.page.AbstractBusinessQueryPage;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractSaleListPage<QUERY,RESULT> extends AbstractBusinessQueryPage<Sale, QUERY, RESULT> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject protected SaleBusiness saleBusiness;
	
	protected BalanceType balanceType;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		String bt = requestParameter(CompanyWebManager.getInstance().getRequestParameterBalanceType());
		if(StringUtils.isNotBlank(bt))
			balanceType=BalanceType.valueOf(bt);
		else
			balanceType = null;
		
		table.setShowHeader(Boolean.FALSE);
		table.setShowFooter(Boolean.TRUE);
		
		((Commandable)table.getAddRowCommandable()).getButton().setRendered(Boolean.FALSE);
		table.getColumnListeners().add(new ColumnAdapter(){
			@Override
			public Boolean isColumn(Field field) {
				return field.getName().equals("balance") && BalanceType.ZERO.equals(balanceType);
			}
			/*
			@Override
			public void fields(List<Field> fields) {
				super.fields(fields);
				for(int i=0;i<fields.size();i++){
					if(fields.get(i).getName().equals("balance") && BalanceType.ZERO.equals(balanceType)){
						fields.remove(i);
						break;
					}
				}
			}*/
		});
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
	/*
	@Override
	protected String componentId() {
		return "saletabview:query";
		//return super.componentId();
	}*/
	
	@Override
	protected Class<Sale> __entityClass__() {
		return Sale.class;
	}

	@Override
	protected Collection<Sale> __query__() {
		SaleSearchCriteria criteria = searchCriteria();
		criteria.getBalanceTypes().clear();
		if(balanceType!=null)
			criteria.getBalanceTypes().add(balanceType);
		
		criteria.getReadConfig().setFirstResultIndex(queryFirst);
		criteria.getReadConfig().setMaximumResultCount(20l);
		table.getColumn("cost").setFooter(numberBusiness.format(saleBusiness.sumCostByCriteria(criteria)));
		if(!BalanceType.ZERO.equals(balanceType)){
			table.getColumn("balance").setFooter(numberBusiness.format(saleBusiness.sumBalanceByCriteria(criteria)));
		}
		debug(criteria.getFromDateSearchCriteria());
		table.getPrintCommandable().setParameter(RootBusinessLayer.getInstance().getParameterFromDate(),criteria.getFromDateSearchCriteria().getPreparedValue().getTime());
		table.getPrintCommandable().setParameter(RootBusinessLayer.getInstance().getParameterToDate(),criteria.getToDateSearchCriteria().getPreparedValue().getTime());
		if(balanceType!=null)
			table.getPrintCommandable().setParameter(CompanyReportRepository.getInstance().getParameterBalanceType(),balanceType.name());
		return saleBusiness.findByCriteria(criteria);
	}
	
	@Override
	protected Long __count__() {
		SaleSearchCriteria criteria = searchCriteria();
		criteria.getBalanceTypes().clear();
		if(balanceType!=null)
			criteria.getBalanceTypes().add(balanceType);
		return saleBusiness.countByCriteria(criteria);
	}

	protected abstract SaleSearchCriteria searchCriteria();
		
}