package org.cyk.system.company.ui.web.primefaces.page;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.model.product.BalanceType;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.company.ui.web.primefaces.model.SaleFormModel;
import org.cyk.ui.api.command.CommandAdapter;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.api.model.table.Cell;
import org.cyk.ui.api.model.table.Column;
import org.cyk.ui.api.model.table.Row;
import org.cyk.ui.web.api.WebNavigationManager;
import org.cyk.ui.web.primefaces.Commandable;
import org.cyk.ui.web.primefaces.page.AbstractBusinessQueryPage;
import org.cyk.utility.common.model.table.TableAdapter;

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
		table.setShowFooter(Boolean.TRUE);
		((Commandable)table.getAddRowCommandable()).getButton().setRendered(Boolean.FALSE);
		table.getTableListeners().add(new TableAdapter<Row<Object>, Column, Object, String, Cell, String>(){
			@Override
			public Boolean ignore(Field field) {
				return field.getName().equals("balance") && BalanceType.ZERO.equals(balanceType);
			}
			
			@Override
			public void fields(List<Field> fields) {
				super.fields(fields);
				for(int i=0;i<fields.size();i++){
					if(fields.get(i).getName().equals("balance") && BalanceType.ZERO.equals(balanceType)){
						fields.remove(i);
						break;
					}
				}
			}
		});
		
		showGraphics = Boolean.TRUE;
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();

		table.setShowEditColumn(Boolean.FALSE);
		table.setShowOpenCommand(Boolean.TRUE);
		((Commandable)table.getOpenRowCommandable()).getButton().setRendered(Boolean.TRUE);
		((Commandable)table.getRemoveRowCommandable()).getButton().setRendered(Boolean.FALSE);
		
		table.setShowToolBar(Boolean.TRUE);
		table.setShowFooter(Boolean.TRUE);
		
		table.getOpenRowCommandable().getCommand().getCommandListeners().add(new CommandAdapter(){
			private static final long serialVersionUID = 1120566504648934547L;
			@Override
			public void serve(UICommand command, Object parameter) {
				@SuppressWarnings("unchecked")
				Sale sale = ((Row<SaleFormModel>)parameter).getData().getIdentifiable();
				WebNavigationManager.getInstance().redirectTo(businessEntityInfos.getUiConsultViewId(), 
						new Object[]{webManager.getRequestParameterIdentifiable(),sale.getIdentifier().toString()});
			}
		});
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
		table.getColumn("cost").setFooter(saleBusiness.sumCostByCriteria(criteria).toString());
		if(!BalanceType.ZERO.equals(balanceType)){
			table.getColumn("balance").setFooter(saleBusiness.sumBalanceByCriteria(criteria).toString());
		}
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