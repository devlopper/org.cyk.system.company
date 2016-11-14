package org.cyk.system.company.business.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.AbstractSale;
import org.cyk.system.root.business.api.TypedBusiness;

public interface AbstractSaleBusiness<SALE extends AbstractSale,SEARCH_CRITERIA extends AbstractSale.SearchCriteria> extends TypedBusiness<SALE> {

	SALE instanciateOne(String code,String customerCode,Object[][] salableProducts);
	
	Collection<SALE> findByCriteria(SEARCH_CRITERIA criteria);
	Long countByCriteria(SEARCH_CRITERIA criteria);
	/*
	SaleResults computeByCriteria(SaleSearchCriteria criteria);
	
	ReportBasedOnTemplateFile<SaleReport> findReport(Sale sale);
	*/
	/**/
	/*
	CartesianModel findTurnOverStatistics(SaleSearchCriteria saleSearchCriteria,TimeDivisionType timeDivisionType);
	CartesianModel findCountStatistics(SaleSearchCriteria saleSearchCriteria,TimeDivisionType timeDivisionType);
	*/
	
	/**/
	
}
