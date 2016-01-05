package org.cyk.system.company.business.api.sale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.cyk.system.company.model.product.ProductEmployee;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.model.sale.SaleSearchCriteria;
import org.cyk.system.company.model.sale.SalesDetails;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.model.file.report.ReportBasedOnTemplateFile;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.utility.common.cdi.AbstractBean;

public interface SaleBusiness extends TypedBusiness<Sale> {

	Sale newInstance(Person person);
	
	SaleProduct selectProduct(Sale sale,SalableProduct salableProduct,BigDecimal quantity);
	SaleProduct selectProduct(Sale sale,SalableProduct salableProduct);
	void unselectProduct(Sale sale,SaleProduct saleProduct);
	void applyChange(Sale sale, SaleProduct saleProduct);
	
	void create(Sale sale,SaleCashRegisterMovement saleCashRegisterMovement,Boolean produceReport);
	void create(Sale sale,SaleCashRegisterMovement saleCashRegisterMovement);
	
	void complete(Sale sale,SaleCashRegisterMovement saleCashRegisterMovement,Boolean produceReport);
	void complete(Sale sale,SaleCashRegisterMovement saleCashRegisterMovement);
	
	Collection<Sale> findByCriteria(SaleSearchCriteria criteria);
	Long countByCriteria(SaleSearchCriteria criteria);
	
	SalesDetails computeByCriteria(SaleSearchCriteria criteria);
	
	ReportBasedOnTemplateFile<SaleReport> findReport(Collection<Sale> sales);
	ReportBasedOnTemplateFile<SaleReport> findReport(Sale sale);
	
	void updateDelivery(Sale sale,Collection<ProductEmployee> productEmployees);
	
	
	/**/
	/*
	CartesianModel findTurnOverStatistics(SaleSearchCriteria saleSearchCriteria,TimeDivisionType timeDivisionType);
	CartesianModel findCountStatistics(SaleSearchCriteria saleSearchCriteria,TimeDivisionType timeDivisionType);
	*/
	
	/**/
	
	Collection<SaleBusinessListener> LISTENERS = new ArrayList<>(); 
	
	public static interface SaleBusinessListener{
		
		void reportCreated(SaleBusiness saleBusiness,SaleReport saleReport,Boolean invoice);
		
	}
	
	public static class SaleBusinessAdapter extends AbstractBean implements SaleBusinessListener{

		private static final long serialVersionUID = -855978096046996503L;

		@Override public void reportCreated(SaleBusiness saleBusiness, SaleReport saleReport,Boolean invoice) {}
		
	}
}
