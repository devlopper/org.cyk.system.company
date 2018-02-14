package org.cyk.system.company.business.api.sale;

import java.util.List;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.model.sale.SaleResults;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;

public interface SaleBusiness extends AbstractSaleBusiness<Sale,Sale.SearchCriteria> {

	@Deprecated
	Sale instanciateOne(String computedIdentifier,String cashierPersonCode,String customerRegistrationCode,String date,String taxable,String[][] salableProductInfos);
	@Deprecated
	List<Sale> instanciateMany(Object[][] arguments);

	SaleResults computeByCriteria(Sale.SearchCriteria criteria);
	
	@Deprecated
	ReportBasedOnTemplateFile<SaleReport> findReport(Sale sale);
	
}
