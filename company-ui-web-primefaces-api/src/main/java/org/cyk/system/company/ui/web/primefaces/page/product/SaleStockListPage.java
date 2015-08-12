package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.cyk.system.company.business.api.product.AbstractSaleStockBusiness;
import org.cyk.system.company.business.api.product.SaleStockBusiness;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.product.SaleStockReportTableRow;
import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.company.model.product.SaleStockSearchCriteria;

import lombok.Getter;
import lombok.Setter; 

@Named @ViewScoped @Getter @Setter
public class SaleStockListPage extends AbstractSaleStockListPage<SaleStock, SaleStockSearchCriteria> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject protected SaleStockBusiness saleStockBusiness;
	
	private String type;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		type = requestParameter(CompanyReportRepository.getInstance().getParameterSaleStockReportType());
		if(CompanyReportRepository.getInstance().getParameterSaleStockReportInventory().equals(type))
			contentTitle=text("company.report.salestock.inventory.title");
		else if(CompanyReportRepository.getInstance().getParameterSaleStockReportCustomer().equals(type))
			contentTitle=text("company.report.salestock.customer.title");
		
	}
	
	@Override
	protected Boolean ignoreField(Field field) {
		
		if(CompanyReportRepository.getInstance().getParameterSaleStockReportInventory().equals(type))
			return SaleStockReportTableRow.inventoryFieldIgnored(field);
		else if(CompanyReportRepository.getInstance().getParameterSaleStockReportCustomer().equals(type))
			return SaleStockReportTableRow.customerFieldIgnored(field);
		return Boolean.TRUE;
	}
	
	
	@Override
	protected void __beforeFindByCriteria__(SaleStockSearchCriteria criteria) {
		super.__beforeFindByCriteria__(criteria);
		table.getPrintCommandable().setParameter(CompanyReportRepository.getInstance().getParameterSaleStockReportType(),type);
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