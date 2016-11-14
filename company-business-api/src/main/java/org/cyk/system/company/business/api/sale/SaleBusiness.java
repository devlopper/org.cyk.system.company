package org.cyk.system.company.business.api.sale;

import java.util.List;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.model.sale.SaleResults;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.system.root.model.party.person.Person;

public interface SaleBusiness extends AbstractSaleBusiness<Sale,Sale.SearchCriteria> {

	Sale instanciateOne(Person person);
	Sale instanciateOne(String computedIdentifier,String cashierPersonCode,String customerRegistrationCode,String date,String taxable,String[][] salableProductInfos);
	List<Sale> instanciateMany(Object[][] arguments);

	SaleResults computeByCriteria(Sale.SearchCriteria criteria);
	
	ReportBasedOnTemplateFile<SaleReport> findReport(Sale sale);
	
	/**/
	/*
	CartesianModel findTurnOverStatistics(SaleSearchCriteria saleSearchCriteria,TimeDivisionType timeDivisionType);
	CartesianModel findCountStatistics(SaleSearchCriteria saleSearchCriteria,TimeDivisionType timeDivisionType);
	*/
	
	/**/
	
}
