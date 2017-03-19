package org.cyk.system.company.business.impl.integration.enterpriseresourceplanning;

import java.io.Serializable;

import javax.inject.Singleton;

import lombok.Getter;

@Singleton @Getter
public class EnterpriseResourcePlanningFakedDataProducer extends AbstractEnterpriseResourcePlanningFakedDataProducer implements Serializable {

	private static final long serialVersionUID = -1832900422621121762L;
	
	@Override
	protected void structure(Listener listener) {
		/*Collection<TangibleProduct> tangibleProducts = inject(TangibleProductBusiness.class).instanciateMany(new String[][]{
				new String[]{"TP1"},new String[]{"TP2"},new String[]{"TP3"},new String[]{"TP4"},new String[]{"TP5"}
		});
		flush(TangibleProduct.class, tangibleProducts);
		Collection<IntangibleProduct> intangibleProducts = inject(IntangibleProductBusiness.class).instanciateMany(new String[][]{
				new String[]{"IP1"},new String[]{"IP2"},new String[]{"IP3"},new String[]{"IP4"},new String[]{"IP5"}
		});
		flush(IntangibleProduct.class, intangibleProducts);
		
		Collection<StockableTangibleProduct> stockableTangibleProducts = inject(StockableTangibleProductBusiness.class).instanciateMany(new String[][]{
				new String[]{"TP2"},new String[]{"TP3"},new String[]{"TP5"}
		});
		flush(StockableTangibleProduct.class, stockableTangibleProducts);
		
		Collection<StockTangibleProductMovement> stockTangibleProductMovements = inject(StockTangibleProductMovementBusiness.class).instanciateMany(new String[][]{
				new String[]{"TP2","100"},new String[]{"TP3","100"},new String[]{"TP5","100"}
		});
		flush(StockTangibleProductMovement.class, stockTangibleProductMovements);
		
		Collection<SalableProduct> salableProducts = inject(SalableProductBusiness.class).instanciateMany(new String[][]{
				new String[]{"TP2","1000"},new String[]{"TP5","2500"},new String[]{"IP5"}
		});
		flush(SalableProduct.class, salableProducts);
		*/
		//inject(CustomerBusiness.class).create(inject(CustomerBusiness.class).instanciateManyRandomly(5));
	}

	@Override
	protected void doBusiness(Listener listener) {
		/*Collection<Sale> sales = inject(SaleBusiness.class).instanciateMany(new Object[][]{
				new Object[]{"sale001",cashierDao.readOneRandomly().getPerson().getCode(),customerDao.readOneRandomly().getCode(),"1/1/2000 05:00","false"
						,new String[][]{ new String[]{"TP2","2"} }}
				,new Object[]{"sale002",cashierDao.readOneRandomly().getPerson().getCode(),customerDao.readOneRandomly().getCode(),"1/1/2000 05:15","false"
						,new String[][]{ new String[]{"TP2","1"} }}
				,new Object[]{"sale003",cashierDao.readOneRandomly().getPerson().getCode(),customerDao.readOneRandomly().getCode(),"1/1/2000 08:00","false"
						,new String[][]{ new String[]{"TP2","1"},new String[]{"TP5","3"} }}
		});
		flush(Sale.class, sales);*/
	}

	
}
