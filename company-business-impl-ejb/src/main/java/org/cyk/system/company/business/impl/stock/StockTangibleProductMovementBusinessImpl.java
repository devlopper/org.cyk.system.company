package org.cyk.system.company.business.impl.stock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.stock.StockTangibleProductMovementBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.sale.SaleBusinessImpl;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.model.stock.StockTangibleProductMovementSearchCriteria;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.company.persistence.api.product.TangibleProductDao;
import org.cyk.system.company.persistence.api.stock.StockTangibleProductMovementDao;
import org.cyk.system.company.persistence.api.stock.StockableTangibleProductDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.business.impl.RootBusinessLayer;

@Stateless
public class StockTangibleProductMovementBusinessImpl extends AbstractTypedBusinessService<StockTangibleProductMovement, StockTangibleProductMovementDao> implements StockTangibleProductMovementBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject private StockableTangibleProductDao stockableTangibleProductDao;
	@Inject private TangibleProductDao tangibleProductDao;
	
	@Inject
	public StockTangibleProductMovementBusinessImpl(StockTangibleProductMovementDao dao) {
		super(dao);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public StockTangibleProductMovement instanciateOne(String[] arguments) {
		StockTangibleProductMovement stockTangibleProductMovement = new StockTangibleProductMovement();
		stockTangibleProductMovement.setStockableTangibleProduct(stockableTangibleProductDao.readByTangibleProduct(tangibleProductDao.read(arguments[0])));
		BigDecimal value = numberBusiness.parseBigDecimal(arguments[1]);
		stockTangibleProductMovement.setMovement(RootBusinessLayer.getInstance().getMovementBusiness()
				.instanciateOne(stockTangibleProductMovement.getStockableTangibleProduct().getMovementCollection(), value.compareTo(BigDecimal.ZERO) >= 0));
		stockTangibleProductMovement.getMovement().setValue(value);
		return stockTangibleProductMovement;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<StockTangibleProductMovement> instanciateMany(String[][] arguments) {
		List<StockTangibleProductMovement> list = new ArrayList<>();
		for(String[] info : arguments)
			list.add(instanciateOne(info));
		return list;
	}
	
	@Override
	public StockTangibleProductMovement create(StockTangibleProductMovement stockTangibleProductMovement) {
		RootBusinessLayer.getInstance().getMovementBusiness().create(stockTangibleProductMovement.getMovement());
		//updateStock(stockTangibleProductMovement);
		stockTangibleProductMovement = super.create(stockTangibleProductMovement);
		logIdentifiable("Created", stockTangibleProductMovement);
		return stockTangibleProductMovement;
	}
	
	@Override
	public void consume(Sale sale) {
		/*
		 * We need to update the stock
		 */
		Collection<SaleProduct> saleProducts = CompanyBusinessLayer.getInstance().getSaleProductDao().readBySale(sale);
		Collection<TangibleProduct> tangibleProducts = new LinkedHashSet<>();
		for(SaleProduct saleProduct : saleProducts)
			if(saleProduct.getSalableProduct().getProduct() instanceof TangibleProduct)
				tangibleProducts.add((TangibleProduct) saleProduct.getSalableProduct().getProduct());
	
		for(TangibleProduct tangibleProduct : tangibleProducts){
			StockableTangibleProduct stockableTangibleProduct = CompanyBusinessLayer.getInstance().getStockableTangibleProductDao().readByTangibleProduct(tangibleProduct);
			if(stockableTangibleProduct==null)
				;
			else{
				BigDecimal count = BigDecimal.ZERO;
				for(SaleProduct saleProduct : saleProducts)
					if(saleProduct.getSalableProduct().getProduct().equals(stockableTangibleProduct.getTangibleProduct()))
						count = count.add(saleProduct.getQuantity());
				StockTangibleProductMovement stockTangibleProductMovement = new StockTangibleProductMovement(stockableTangibleProduct
						,RootBusinessLayer.getInstance().getMovementBusiness().instanciateOne(stockableTangibleProduct.getMovementCollection(), Boolean.FALSE));
				stockTangibleProductMovement.getMovement().setValue(count.negate());
				CompanyBusinessLayer.getInstance().getStockTangibleProductMovementBusiness().create(stockTangibleProductMovement);
				//logTrace("Updated : {}",stockableTangibleProduct.getLogMessage());
			}
		}
	}
	
	/*
	private void updateStock(TangibleProductStockMovement tangibleProductStockMovement){
		StockConfiguration stockConfiguration = accountingPeriodBusiness.findCurrent().getStockConfiguration();
		BigDecimal stock = tangibleProductStockMovement.get;
		if(stock==null)
			stock = BigDecimal.ZERO;
		BigDecimal newStock = stock.add(tangibleProductStockMovement.getMovement().getValue());
		exceptionUtils().exception(newStock.signum()==-1, "tangibleproduct.stock.negative");
		tangibleProductStockMovement.getTangibleProduct().setStockQuantity(newStock);
		productDao.update(tangibleProductStockMovement.getTangibleProduct());
	}*/

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<StockTangibleProductMovement> findByCriteria(StockTangibleProductMovementSearchCriteria criteria) {
		prepareFindByCriteria(criteria);
		return dao.readByCriteria(criteria);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Long countByCriteria(StockTangibleProductMovementSearchCriteria criteria) {
		return dao.countByCriteria(criteria);
	}
	
	/**/
	
	public static class SaleBusinessAdapter extends SaleBusinessImpl.Listener.Adapter implements Serializable {
		private static final long serialVersionUID = 5585791722273454192L;
		
		@Override
		public void processOnConsume(Sale sale, Crud crud) {
			companyBusinessLayer.getStockTangibleProductMovementBusiness().consume(sale);
		}
	}
}
