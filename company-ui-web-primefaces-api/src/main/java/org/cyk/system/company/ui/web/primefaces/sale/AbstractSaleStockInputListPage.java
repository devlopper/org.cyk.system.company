package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.sale.AbstractSaleStockTangibleProductMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleStockTangibleProductMovementInputBusiness;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.sale.SaleStockReportTableRow;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementInput;
import org.cyk.system.company.model.sale.SaleStockInputSearchCriteria;
import org.cyk.system.company.model.sale.SaleStocksDetails;
import org.cyk.ui.web.primefaces.Commandable;

@Getter @Setter
public abstract class AbstractSaleStockInputListPage extends AbstractSaleStockListPage<SaleStockTangibleProductMovementInput, SaleStockInputSearchCriteria> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject protected SaleStockTangibleProductMovementInputBusiness saleStockInputBusiness;
	
	@Override
	protected Boolean ignoreField(Field field) {
		return SaleStockReportTableRow.inputFieldIgnored(field);
	}
	
	@Override
	protected void initialisation() {
		super.initialisation();
		rowAdapter.setOpenable(Boolean.TRUE);
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		//TODO to be deleted : auto called by super
		/*
		table.getOpenRowCommandable().getCommand().getCommandListeners().add(new CommandAdapter(){
			private static final long serialVersionUID = 1120566504648934547L;
			@Override
			public void serve(UICommand command, Object parameter) {
				@SuppressWarnings("unchecked")
				Sale sale = ((SaleStockInput)((Row<SaleStockReportTableRow>)parameter).getData().getSaleStock()).getSale();
				WebNavigationManager.getInstance().redirectTo(businessEntityInfos.getUiConsultViewId(), 
						new Object[]{webManager.getRequestParameterIdentifiable(),sale.getIdentifier().toString()});
			}
		});*/
		
		((Commandable)table.getOpenRowCommandable()).getButton().setRendered(Boolean.TRUE);
		((Commandable)table.getAddRowCommandable()).getButton().setRendered(Boolean.TRUE);
		
	}
		
	@Override
	protected void __afterFindByCriteria__(SaleStockInputSearchCriteria criteria,Collection<SaleStockTangibleProductMovementInput> results) {
		super.__afterFindByCriteria__(criteria, results);
		table.getPrintCommandable().setParameter(CompanyReportRepository.getInstance().getParameterSaleStockReportType(),
				CompanyReportRepository.getInstance().getParameterSaleStockReportInput());
		SaleStocksDetails details = saleStockInputBusiness.computeByCriteria(criteria);
		
		table.setColumnFooter(SaleStockReportTableRow.FIELD_NUMBER_OF_GOODS, details.getIn());
		table.setColumnFooter(SaleStockReportTableRow.FIELD_REMAINING_NUMBER_OF_GOODS, details.getRemaining());
		table.setColumnFooter(SaleStockReportTableRow.FIELD_AMOUNT, details.getSalesDetails().getCost());
		table.setColumnFooter(SaleStockReportTableRow.FIELD_BALANCE, details.getSalesDetails().getBalance());
	}
		
	@Override
	protected Class<SaleStockTangibleProductMovementInput> __entityClass__() {
		return SaleStockTangibleProductMovementInput.class;
	}
	
	@Override
	protected Class<SaleStockInputSearchCriteria> searchCriteriaClass() {
		return SaleStockInputSearchCriteria.class;
	}
	
	@Override
	protected AbstractSaleStockTangibleProductMovementBusiness<SaleStockTangibleProductMovementInput, SaleStockInputSearchCriteria> business() {
		return saleStockInputBusiness;
	}
	
}