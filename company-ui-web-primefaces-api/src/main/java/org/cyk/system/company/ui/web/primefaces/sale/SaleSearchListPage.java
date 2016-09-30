package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.sale.SaleDetails;
import org.cyk.system.company.model.payment.BalanceType;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleResults;
import org.cyk.system.company.model.sale.SaleSearchCriteria;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.company.ui.web.primefaces.model.SaleQueryFormModel;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.ui.web.primefaces.page.AbstractBusinessQueryPage;

@Getter @Setter @Named @ViewScoped
public class SaleSearchListPage extends AbstractBusinessQueryPage<Sale,SaleQueryFormModel, SaleDetails> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	private BalanceType balanceType;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		String bt = requestParameter(CompanyWebManager.getInstance().getRequestParameterBalanceType());
		if(StringUtils.isNotBlank(bt))
			balanceType=BalanceType.valueOf(bt);
		else
			balanceType = null;
		
		/*
		table.setShowHeader(Boolean.FALSE);
		table.setShowFooter(Boolean.TRUE);
		
		((Commandable)table.getAddRowCommandable()).getButton().setRendered(Boolean.FALSE);
		table.getColumnListeners().add(new ColumnAdapter(){
			@Override
			public Boolean isColumn(Field field) {
				return field.getName().equals("balance") && BalanceType.ZERO.equals(balanceType);
			}
		});
		
		rowAdapter.setOpenable(Boolean.TRUE);
		*/
		
		rowAdapter.setOpenable(Boolean.TRUE);
		rowAdapter.setUpdatable(Boolean.TRUE);
		table.setShowHeader(Boolean.TRUE);
		table.setShowToolBar(Boolean.TRUE);
		
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		/*
		table.setShowEditColumn(Boolean.FALSE);		
		
		((Commandable)table.getOpenRowCommandable()).getButton().setRendered(Boolean.TRUE);
		((Commandable)table.getRemoveRowCommandable()).getButton().setRendered(Boolean.FALSE);
		
		table.setShowToolBar(Boolean.TRUE);
		
		table.setShowEditColumn(Boolean.FALSE);
		table.setShowAddRemoveColumn(Boolean.FALSE);
		table.getPrintCommandable().setRendered(Boolean.TRUE);
		*/
		table.setShowOpenCommand(Boolean.TRUE);
	}
		
	@Override
	protected Boolean autoLoad() {
		return Boolean.TRUE;
	}
	
	@Override
	protected Class<SaleQueryFormModel> __queryClass__() {
		return SaleQueryFormModel.class;
	}

	@Override
	protected Class<SaleDetails> __resultClass__() {
		return SaleDetails.class;
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
		criteria.getReadConfig().setFirstResultIndex(queryFirst);
		criteria.getReadConfig().setMaximumResultCount(20l);
		SaleResults results = inject(SaleBusiness.class).computeByCriteria(criteria); 
		table.getColumn(SaleDetails.FIELD_COST).setFooter(numberBusiness.format(results.getCost().getValue()));
		if(!BalanceType.ZERO.equals(balanceType)){
			table.getColumn(SaleDetails.FIELD_BALANCE).setFooter(numberBusiness.format(results.getBalance()));
		}
		
		table.getPrintCommandable().setParameter(RootBusinessLayer.getInstance().getParameterFromDate(),criteria.getFromDateSearchCriteria().getPreparedValue().getTime());
		table.getPrintCommandable().setParameter(RootBusinessLayer.getInstance().getParameterToDate(),criteria.getToDateSearchCriteria().getPreparedValue().getTime());
		if(balanceType!=null)
			table.getPrintCommandable().setParameter(CompanyReportRepository.getInstance().getParameterBalanceType(),balanceType.name());
		return inject(SaleBusiness.class).findByCriteria(criteria);
	}
	
	@Override
	protected Long __count__() {
		return inject(SaleBusiness.class).countByCriteria(searchCriteria());
	}

	protected SaleSearchCriteria searchCriteria(){
		SaleSearchCriteria criteria = new SaleSearchCriteria(form.getData().getFromDate(),form.getData().getToDate());
		criteria.getBalanceTypes().clear();
		if(balanceType!=null)
			criteria.getBalanceTypes().add(balanceType);
		processSearchCriteria(criteria);
		if(criteria.getFiniteStateMachineStates().isEmpty())
			criteria.getFiniteStateMachineStates().add(inject(AccountingPeriodBusiness.class).findCurrent().getSaleConfiguration().getFiniteStateMachine().getInitialState());
		return criteria;
	}
	
	protected void processSearchCriteria(SaleSearchCriteria saleSearchCriteria){
		
	}
		
}