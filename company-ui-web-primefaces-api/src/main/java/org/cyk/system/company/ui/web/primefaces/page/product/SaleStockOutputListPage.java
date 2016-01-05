package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.sale.AbstractSaleStockBusiness;
import org.cyk.system.company.business.api.sale.SaleStockOutputBusiness;
import org.cyk.system.company.business.impl.sale.SaleStockReportTableRow;
import org.cyk.system.company.model.sale.SaleSearchCriteria;
import org.cyk.system.company.model.sale.SaleStockOutput;
import org.cyk.system.company.model.sale.SaleStockOutputSearchCriteria;
import org.cyk.system.company.model.sale.SaleStocksDetails;

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
	protected void __afterFindByCriteria__(SaleStockOutputSearchCriteria criteria,Collection<SaleStockOutput> results) {
		super.__afterFindByCriteria__(criteria, results);
		SaleStocksDetails details = saleStockOutputBusiness.computeByCriteria(criteria);
		table.setColumnFooter(SaleStockReportTableRow.FIELD_TAKEN_NUMBER_OF_GOODS,details.getOut().abs());
		table.setColumnFooter(SaleStockReportTableRow.FIELD_AMOUNT_PAID,details.getSalesDetails().getPaid());
		table.setColumnFooter(SaleStockReportTableRow.FIELD_BALANCE,details.getSalesDetails().getBalance());
	}
		
	@Override
	protected SaleStockOutputSearchCriteria searchCriteria() {
		SaleStockOutputSearchCriteria criteria = super.searchCriteria();
		criteria.setSaleSearchCriteria(new SaleSearchCriteria(criteria));
		criteria.getSaleSearchCriteria().setHasAtLeastOneCashRegisterMovement(Boolean.TRUE);
		return criteria;
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