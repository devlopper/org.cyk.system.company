package org.cyk.system.company.business.api.sale;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.model.sale.SaleResults;
import org.cyk.system.company.model.sale.SaleSearchCriteria;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineAlphabet;
import org.cyk.system.root.model.party.person.Person;

public interface SaleBusiness extends TypedBusiness<Sale> {

	Sale instanciate(Person person);
	Sale instanciate(String computedIdentifier,String cashierPersonCode,String customerRegistrationCode,String date,String taxable,String[][] salableProductInfos);
	List<Sale> instanciate(Object[][] arguments);
	
	SaleProduct selectProduct(Sale sale,SalableProduct salableProduct,BigDecimal quantity);
	SaleProduct selectProduct(Sale sale,SalableProduct salableProduct);
	void unselectProduct(Sale sale,SaleProduct saleProduct);
	void applyChange(Sale sale, SaleProduct saleProduct);
	
	Sale create(Sale sale,SaleCashRegisterMovement saleCashRegisterMovement);
	void update(Sale sale,FiniteStateMachineAlphabet finiteStateMachineAlphabet);
	
	Collection<Sale> findByCriteria(SaleSearchCriteria criteria);
	Long countByCriteria(SaleSearchCriteria criteria);
	
	SaleResults computeByCriteria(SaleSearchCriteria criteria);
	
	//ReportBasedOnTemplateFile<SaleReport> findReport(Collection<Sale> sales);
	ReportBasedOnTemplateFile<SaleReport> findReport(Sale sale);
	
	Sale findByComputedIdentifier(String identifier);
	
	/**/
	/*
	CartesianModel findTurnOverStatistics(SaleSearchCriteria saleSearchCriteria,TimeDivisionType timeDivisionType);
	CartesianModel findCountStatistics(SaleSearchCriteria saleSearchCriteria,TimeDivisionType timeDivisionType);
	*/
	
	/**/
	
}
