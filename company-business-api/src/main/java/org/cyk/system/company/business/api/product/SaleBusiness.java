package org.cyk.system.company.business.api.product;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductEmployee;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.company.model.product.SaleReport;
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.model.file.report.Report;

public interface SaleBusiness extends TypedBusiness<Sale> {

	SaleProduct selectProduct(Sale sale,Product product);
	SaleProduct selectProduct(Sale sale,Product product,BigDecimal quantity);
	void unselectProduct(Sale sale,SaleProduct saleProduct);
	void quantifyProduct(Sale sale,SaleProduct saleProduct);

	void create(Sale sale,SaleCashRegisterMovement saleCashRegisterMovement);
	
	Collection<Sale> findByCriteria(SaleSearchCriteria criteria);
	Long countByCriteria(SaleSearchCriteria criteria);
	
	BigDecimal sumCostByCriteria(SaleSearchCriteria criteria);
	BigDecimal sumValueAddedTaxByCriteria(SaleSearchCriteria criteria);
	BigDecimal sumBalanceByCriteria(SaleSearchCriteria criteria);
	
	Report<SaleReport> findReport(Collection<Sale> sales);
	
	void updateDelivery(Sale sale,Collection<ProductEmployee> productEmployees);
	
	/**/
	/*
	CartesianModel findTurnOverStatistics(SaleSearchCriteria saleSearchCriteria,TimeDivisionType timeDivisionType);
	CartesianModel findCountStatistics(SaleSearchCriteria saleSearchCriteria,TimeDivisionType timeDivisionType);
	*/
}
