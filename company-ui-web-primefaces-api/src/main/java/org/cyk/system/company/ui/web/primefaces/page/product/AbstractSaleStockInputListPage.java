package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.AbstractSaleStockBusiness;
import org.cyk.system.company.business.api.product.SaleStockInputBusiness;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.product.SaleStockReportTableRow;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockInputSearchCriteria;
import org.cyk.system.company.model.product.SaleStocksDetails;
import org.cyk.ui.api.command.CommandAdapter;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.api.model.table.Row;
import org.cyk.ui.web.api.WebNavigationManager;
import org.cyk.ui.web.primefaces.Commandable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractSaleStockInputListPage extends AbstractSaleStockListPage<SaleStockInput, SaleStockInputSearchCriteria> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject protected SaleStockInputBusiness saleStockInputBusiness;
	
	@Override
	protected Boolean ignoreField(Field field) {
		return SaleStockReportTableRow.inputFieldIgnored(field);
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();

		table.getOpenRowCommandable().getCommand().getCommandListeners().add(new CommandAdapter(){
			private static final long serialVersionUID = 1120566504648934547L;
			@Override
			public void serve(UICommand command, Object parameter) {
				@SuppressWarnings("unchecked")
				Sale sale = ((SaleStockInput)((Row<SaleStockReportTableRow>)parameter).getData().getSaleStock()).getSale();
				WebNavigationManager.getInstance().redirectTo(businessEntityInfos.getUiConsultViewId(), 
						new Object[]{webManager.getRequestParameterIdentifiable(),sale.getIdentifier().toString()});
			}
		});
		
		((Commandable)table.getOpenRowCommandable()).getButton().setRendered(Boolean.TRUE);
		((Commandable)table.getAddRowCommandable()).getButton().setRendered(Boolean.TRUE);
	}
		
	@Override
	protected void __afterFindByCriteria__(SaleStockInputSearchCriteria criteria,Collection<SaleStockInput> results) {
		super.__afterFindByCriteria__(criteria, results);
		table.getPrintCommandable().setParameter(CompanyReportRepository.getInstance().getParameterSaleStockReportType(),
				CompanyReportRepository.getInstance().getParameterSaleStockReportInput());
		SaleStocksDetails details = saleStockInputBusiness.computeByCriteria(searchCriteria());
		table.getColumn(SaleStockReportTableRow.FIELD_NUMBER_OF_GOODS).setFooter(numberBusiness.format(details.getIn()));
		table.getColumn(SaleStockReportTableRow.FIELD_REMAINING_NUMBER_OF_GOODS).setFooter(numberBusiness.format(details.getRemaining()));
		table.getColumn(SaleStockReportTableRow.FIELD_AMOUNT).setFooter(numberBusiness.format(details.getSalesDetails().getCost()));
		table.getColumn(SaleStockReportTableRow.FIELD_BALANCE).setFooter(numberBusiness.format(details.getSalesDetails().getBalance()));
	}
	
	@Override
	protected void rowAdded(Row<Object> row) {
		super.rowAdded(row);
		row.setOpenable(Boolean.TRUE);
	}
	
	@Override
	protected Class<SaleStockInput> __entityClass__() {
		return SaleStockInput.class;
	}
	
	@Override
	protected Class<SaleStockInputSearchCriteria> searchCriteriaClass() {
		return SaleStockInputSearchCriteria.class;
	}
	
	@Override
	protected AbstractSaleStockBusiness<SaleStockInput, SaleStockInputSearchCriteria> business() {
		return saleStockInputBusiness;
	}
	
}