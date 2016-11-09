package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.impl.sale.SaleBusinessImpl;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCollection;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.root.business.api.Crud;

@Stateless
public class ProductBusinessImpl extends AbstractProductBusinessImpl<Product,ProductDao> implements ProductBusiness,Serializable  {
	
	private static final long serialVersionUID = 2801588592108008404L;

	@Inject
    public ProductBusinessImpl(ProductDao dao) {
        super(dao);
    }
	
	@Override
	protected Set<Product> products(Collection<SalableProductCollectionItem> saleProducts) {
		Set<Product> products = new HashSet<>(),noProductCollections= new HashSet<>();
		Set<ProductCollection> productCollections= new HashSet<>();
		for(SalableProductCollectionItem saleProduct : saleProducts)
			if(saleProduct.getSalableProduct().getProduct() instanceof ProductCollection)
				productCollections.add((ProductCollection) saleProduct.getSalableProduct().getProduct());
			else
				noProductCollections.add(saleProduct.getSalableProduct().getProduct());
		/*
		for(ProductCollection collection : productCollections){
			if(Boolean.TRUE.equals(collection.getSalable()))
				products.add(collection);
			noProductCollections.addAll(dao.readByCollection(collection));
		}
		
		for(Product product : noProductCollections)
			if(Boolean.TRUE.equals(product.getSalable()))
				products.add(product);
		*/
		return products;
	}
	
	@Override
	protected void beforeUpdate(Product product, BigDecimal usedCount) {
		if(product instanceof TangibleProduct)
			TangibleProductBusinessImpl.__beforeUpdate__((TangibleProduct) product, usedCount);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<Product> findByCollection(ProductCollection collection) {
		return dao.readByCollection(collection);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<Product> findNotCollectionBySale(Sale sale) {
		Collection<Product> products = new ArrayList<>();
		/*for(SalableProductCollectionItem saleProduct : sale.getSaleProducts())
			if(saleProduct.getSalableProduct().getProduct() instanceof ProductCollection)
				for(Product product : dao.readByCollection((ProductCollection) saleProduct.getSalableProduct().getProduct()))
					products.add(product);
			else
				products.add(saleProduct.getSalableProduct().getProduct());
		*/
		return products;
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<Product> findToDelivery(Sale sale) {
		Collection<Product> products = new ArrayList<>(findNotCollectionBySale(sale));
		/*for(ProductEmployee productEmployee : sale.getPerformers())
			products.remove(productEmployee.getProduct());*/
		return products;
	}
	
	/**/
	
	public static class SaleBusinessAdapter extends SaleBusinessImpl.Listener.Adapter implements Serializable {
		private static final long serialVersionUID = 5585791722273454192L;
		
		/*@Override
		public void processOnConsume(Sale sale, Crud crud, Boolean first) {
			inject(ProductBusiness.class).consume(sale,crud,first);
		}*/
	}
 
}
