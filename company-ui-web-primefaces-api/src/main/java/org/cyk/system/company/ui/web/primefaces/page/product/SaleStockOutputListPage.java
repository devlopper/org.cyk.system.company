package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.product.AbstractSaleStockBusiness;
import org.cyk.system.company.business.api.product.SaleStockOutputBusiness;
import org.cyk.system.company.business.impl.product.SaleStockReportTableRow;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.company.model.product.SaleStockOutputSearchCriteria;
import org.cyk.system.company.model.product.SaleStocksDetails;

@Named @ViewScoped @Getter @Setter
public class SaleStockOutputListPage extends AbstractSaleStockListPage<SaleStockOutput, SaleStockOutputSearchCriteria> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject protected SaleStockOutputBusiness saleStockOutputBusiness;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		contentTitle=text("company.report.salestockoutput.cashregister.title");
	}
	
	@Override
	protected void __beforeFindByCriteria__(SaleStockOutputSearchCriteria criteria) {
		super.__beforeFindByCriteria__(criteria);
		table.getPrintCommandable().setParameter(companyReportRepository.getParameterSaleStockReportType(),
				companyReportRepository.getParameterSaleStockReportCashRegister());
	}
		
	@Override
	protected Class<SaleStockOutput> __entityClass__() {
		return SaleStockOutput.class;
	}
	
	@Override
	protected Collection<SaleStockOutput> __query__() {
		Collection<SaleStockOutput> collection = super.__query__();
		SaleStocksDetails details = saleStockOutputBusiness.computeByCriteria(searchCriteria());
		
		table.getColumn(SaleStockReportTableRow.FIELD_TAKEN_NUMBER_OF_GOODS).setFooter(numberBusiness.format(details.getOut().abs()));
		table.getColumn(SaleStockReportTableRow.FIELD_AMOUNT_PAID).setFooter(numberBusiness.format(details.getSalesDetails().getPaid()));
		table.getColumn(SaleStockReportTableRow.FIELD_BALANCE).setFooter(numberBusiness.format(details.getSalesDetails().getBalance()));
		return collection;
	}
	
	/**/
	
	@Override
	protected Boolean ignoreField(Field field) {
		return SaleStockReportTableRow.cashRegisterFieldIgnored(field);
	}

	@Override
	protected Class<SaleStockOutputSearchCriteria> searchCriteriaClass() {
		return SaleStockOutputSearchCriteria.class;
	}

	@Override
	protected AbstractSaleStockBusiness<SaleStockOutput, SaleStockOutputSearchCriteria> business() {
		return saleStockOutputBusiness;
	}

	
}