package org.cyk.system.company.business.impl.integration.own;

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Singleton;

import lombok.Getter;

import org.cyk.system.company.business.impl.AbstractCompanyFakedDataProducer;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.Sale;

@Singleton @Getter
public class OwnFakedDataProducer extends AbstractCompanyFakedDataProducer implements Serializable {

	private static final long serialVersionUID = -1832900422621121762L;
	
	@Override
	protected void structure() {
		/*Collection<TangibleProduct> tangibleProducts = companyBusinessLayer.getTangibleProductBusiness().instanciateMany(new String[][]{
				new String[]{"TP1"},new String[]{"TP2"},new String[]{"TP3"},new String[]{"TP4"},new String[]{"TP5"}
		});
		flush(TangibleProduct.class, tangibleProducts);
		Collection<IntangibleProduct> intangibleProducts = companyBusinessLayer.getIntangibleProductBusiness().instanciateMany(new String[][]{
				new String[]{"IP1"},new String[]{"IP2"},new String[]{"IP3"},new String[]{"IP4"},new String[]{"IP5"}
		});
		flush(IntangibleProduct.class, intangibleProducts);
		
		Collection<StockableTangibleProduct> stockableTangibleProducts = companyBusinessLayer.getStockableTangibleProductBusiness().instanciateMany(new String[][]{
				new String[]{"TP2"},new String[]{"TP3"},new String[]{"TP5"}
		});
		flush(StockableTangibleProduct.class, stockableTangibleProducts);
		
		Collection<StockTangibleProductMovement> stockTangibleProductMovements = companyBusinessLayer.getStockTangibleProductMovementBusiness().instanciateMany(new String[][]{
				new String[]{"TP2","100"},new String[]{"TP3","100"},new String[]{"TP5","100"}
		});
		flush(StockTangibleProductMovement.class, stockTangibleProductMovements);
		
		Collection<SalableProduct> salableProducts = companyBusinessLayer.getSalableProductBusiness().instanciateMany(new String[][]{
				new String[]{"TP2","1000"},new String[]{"TP5","2500"},new String[]{"IP5"}
		});
		flush(SalableProduct.class, salableProducts);
		*/
		rootRandomDataProvider.createActor(Customer.class, 5);
	}

	@Override
	protected void doBusiness(Listener listener) {
		Collection<Sale> sales = companyBusinessLayer.getSaleBusiness().instanciateMany(new Object[][]{
				new Object[]{"sale001",cashierDao.readOneRandomly().getPerson().getCode(),customerDao.readOneRandomly().getCode(),"1/1/2000 05:00","false"
						,new String[][]{ new String[]{"TP2","2"} }}
				,new Object[]{"sale002",cashierDao.readOneRandomly().getPerson().getCode(),customerDao.readOneRandomly().getCode(),"1/1/2000 05:15","false"
						,new String[][]{ new String[]{"TP2","1"} }}
				,new Object[]{"sale003",cashierDao.readOneRandomly().getPerson().getCode(),customerDao.readOneRandomly().getCode(),"1/1/2000 08:00","false"
						,new String[][]{ new String[]{"TP2","1"},new String[]{"TP5","3"} }}
		});
		flush(Sale.class, sales);
	}

	
}
