package org.cyk.system.company.business.api.sale;

import java.util.Collection;
import java.util.List;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.model.sale.SaleResults;
import org.cyk.system.company.model.sale.SaleSearchCriteria;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineAlphabet;
import org.cyk.system.root.model.party.person.Person;

public interface SaleBusiness extends TypedBusiness<Sale> {

	Sale instanciateOne(Person person);
	Sale instanciateOne(String computedIdentifier,String cashierPersonCode,String customerRegistrationCode,String date,String taxable,String[][] salableProductInfos);
	List<Sale> instanciateMany(Object[][] arguments);

	void update(Sale sale,FiniteStateMachineAlphabet finiteStateMachineAlphabet);
	
	Collection<Sale> findByCriteria(SaleSearchCriteria criteria);
	Long countByCriteria(SaleSearchCriteria criteria);
	
	SaleResults computeByCriteria(SaleSearchCriteria criteria);
	
	//ReportBasedOnTemplateFile<SaleReport> findReport(Collection<Sale> sales);
	ReportBasedOnTemplateFile<SaleReport> findReport(Sale sale);
	
	@Deprecated
	Sale findByComputedIdentifier(String identifier);
	
	/**/
	/*
	CartesianModel findTurnOverStatistics(SaleSearchCriteria saleSearchCriteria,TimeDivisionType timeDivisionType);
	CartesianModel findCountStatistics(SaleSearchCriteria saleSearchCriteria,TimeDivisionType timeDivisionType);
	*/
	
	/**/
	
}
