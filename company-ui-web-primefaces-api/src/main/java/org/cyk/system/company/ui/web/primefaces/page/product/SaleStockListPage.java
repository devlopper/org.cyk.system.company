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
				searchCriteria.getToDateSearchCriteria().getPreparedValue(), rows);
		
		Collection<SaleStockReportTableRow> saleStockReportTableRows = new ArrayList<>();
		
		for(Object r : rows){
			saleStockReportTableRows.add((SaleStockReportTableRow) r);
		}
		
		return saleStockReportTableRows;
	}
	
	/*
	@SuppressWarnings("unchecked")
	@Override
	protected Collection<Object> datas(Collection<SaleStock> identifiables) {
		Collection<Object> rows = new ArrayList<>();
		for(Object object : super.datas(identifiables)){
			rows.add(((SaleStockQueryResultFormModel)object).getSaleStockReportTableRow());
		}
		
		SaleStockSearchCriteria searchCriteria = searchCriteria(); 
		rows = (Collection<Object>) companyReportRepository.processSaleStockReportRows(type, 
				searchCriteria.getFromDateSearchCriteria().getPreparedValue(), 
				searchCriteria.getToDateSearchCriteria().getPreparedValue(), rows);
		
		Collection<Object> nr = new ArrayList<>();
		for(Object r : rows){
			SaleStockQueryResultFormModel v = new SaleStockQueryResultFormModel();
			v.setIdentifiable(((SaleStockReportTableRow)r).getSaleStock());
			v.read();
			nr.add(v);
		}
		//SaleStockSearchCriteria searchCriteria = searchCriteria(); 
		//return (Collection<Object>) companyReportRepository.processSaleStockReportRows(companyReportRepository.getParameterSaleStockReportCashRegister(), 
		//		searchCriteria.getFromDateSearchCriteria().getPreparedValue(), 
		//		searchCriteria.getToDateSearchCriteria().getPreparedValue(), rows); //super.datas(identifiables);
		
		return nr;
	}
*/
	
	@Override
	protected Boolean ignoreField(Field field) {
		
		if(companyReportRepository.getParameterSaleStockReportInventory().equals(type))
			return SaleStockReportTableRow.inventoryFieldIgnored(field);
		else if(companyReportRepository.getParameterSaleStockReportCustomer().equals(type))
			return SaleStockReportTableRow.customerFieldIgnored(field);
		return Boolean.TRUE;
	}
	
	
	@Override
	protected void __beforeFindByCriteria__(SaleStockSearchCriteria criteria) {
		super.__beforeFindByCriteria__(criteria);
		table.getPrintCommandable().setParameter(companyReportRepository.getParameterSaleStockReportType(),type);
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