package org.cyk.system.company.business.api.product;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleReport;
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.company.model.product.SaledProduct;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.business.api.chart.CartesianModel;
import org.cyk.system.root.model.file.report.Report;
import org.cyk.system.root.model.time.TimeDivisionType;

public interface SaleBusiness extends TypedBusiness<Sale> {

	SaledProduct selectProduct(Sale sale,Product product);
	void unselectProduct(Sale sale,SaledProduct saledProduct);
	void quantifyProduct(Sale sale,SaledProduct saledProduct);

	void create(Sale sale,SaleCashRegisterMovement saleCashRegisterMovement);
	
	Collection<Sale> findByCriteria(SaleSearchCriteria criteria);
	Long countByCriteria(SaleSearchCriteria criteria);
	
	BigDecimal sumCostByCriteria(SaleSearchCriteria criteria);
	BigDecimal sumBalanceByCriteria(SaleSearchCriteria criteria);
	
	Report<SaleReport> findReport(Collection<Sale> sales);
	
	/**/
	
	CartesianModel findTurnOverStatistics(SaleSearchCriteria saleSearchCriteria,TimeDivisionType timeDivisionType);
	CartesianModel findCountStatistics(SaleSearchCriteria saleSearchCriteria,TimeDivisionType timeDivisionType);
	
}
