package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.product.AbstractSaleStockBusiness;
import org.cyk.system.company.business.api.product.SaleStockBusiness;
import org.cyk.system.company.business.impl.product.SaleStockReportTableRow;
import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.company.model.product.SaleStockSearchCriteria;
import org.cyk.system.company.model.product.SaleStocksDetails;

@Named @ViewScoped @Getter @Setter
public class SaleStockListPage extends AbstractSaleStockListPage<SaleStock, SaleStockSearchCriteria> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject protected SaleStockBusiness saleStockBusiness;
	
	private String type;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		type = requestParameter(companyReportRepository.getParameterSaleStockReportType());
		if(companyReportRepository.getParameterSaleStockReportInventory().equals(type))
			contentTitle=text("company.report.salestock.inventory.title");
		else if(companyReportRepository.getParameterSaleStockReportCustomer().equals(type))
			contentTitle=text("company.report.salestock.customer.title");
		
	}
		
	@SuppressWarnings("unchecked")
	@Override
	protected Collection<SaleStockReportTableRow> __results__(Collection<SaleStock> saleStocks) {
		Collection<Object> rows = new ArrayList<>();
		for(SaleStockReportTableRow saleStock : super.__results__(saleStocks)){
			rows.add(saleStock);
		}
		
		SaleStockSearchCriteria searchCriteria = searchCriteria(); 
		rows = (Collection<Object>) companyReportRepository.processSaleStockReportRows(type, 
				searchCriteria.getFromDateSearchCriteria().getPreparedValue(), 
				searchCriteria.getToDateSearchCriteria().getPreparedValue(), rows,Boolean.FALSE);
		
		Collection<SaleStockReportTableRow> saleStockReportTableRows = new ArrayList<>();
		
		for(Object r : rows){
			saleStockReportTableRows.add((SaleStockReportTableRow) r);
		}
		
		return saleStockReportTableRows;
	}
	
	@Override
	protected Boolean ignoreField(Field field) {	
		if(companyReportRepository.getParameterSaleStockReportInventory().equals(type))
			return SaleStockReportTableRow.inventoryFieldIgnored(field);
		else if(companyReportRepository.getParameterSaleStockReportCustomer().equals(type))
			return SaleStockReportTableRow.customerFieldIgnored(field);
		return Boolean.TRUE;
	}
	
	@Override
	protected void __afterFindByCriteria__(SaleStockSearchCriteria criteria,Collection<SaleStock> results) {
		super.__afterFindByCriteria__(criteria, results);
		table.getPrintCommandable().setParameter(companyReportRepository.getParameterSaleStockReportType(),type);
		SaleStocksDetails details = saleStockBusiness.computeByCriteria(criteria);
		if(companyReportRepository.getParameterSaleStockReportInventory().equals(type)){
			table.getColumn(SaleStockReportTableRow.FIELD_STOCK_IN).setFooter(numberBusiness.format(details.getIn()));
			table.getColumn(SaleStockReportTableRow.FIELD_STOCK_OUT).setFooter(numberBusiness.format(details.getOut()));
			table.getColumn(SaleStockReportTableRow.FIELD_REMAINING_NUMBER_OF_GOODS).setFooter(numberBusiness.format(details.getRemaining()));	
		}else if(companyReportRepository.getParameterSaleStockReportCustomer().equals(type)) {
			table.getColumn(SaleStockReportTableRow.FIELD_AMOUNT).setFooter(numberBusiness.format(details.getSalesDetails().getCost()));
			table.getColumn(SaleStockReportTableRow.FIELD_AMOUNT_PAID).setFooter(numberBusiness.format(details.getSalesDetails().getPaid()));
			table.getColumn(SaleStockReportTableRow.FIELD_CUMULATED_BALANCE).setFooter(numberBusiness.format(details.getSalesDetails().getBalance()));
		}
		
	}
	
	@Override
	protected Class<SaleStock> __entityClass__() {
		return SaleStock.class;
	}

	@Override
	protected Class<SaleStockSearchCriteria> searchCriteriaClass() {
		return SaleStockSearchCriteria.class;
	}

	@Override
	protected AbstractSaleStockBusiness<SaleStock, SaleStockSearchCriteria> business() {
		return saleStockBusiness;
	}
	
}