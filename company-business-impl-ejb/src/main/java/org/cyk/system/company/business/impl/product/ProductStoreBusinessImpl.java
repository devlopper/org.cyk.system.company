package org.cyk.system.company.business.impl.product;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.api.product.ProductStoreBusiness;
import org.cyk.system.company.business.api.sale.SalableProductStoreBusiness;
import org.cyk.system.company.business.api.stock.StockableProductStoreBusiness;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.model.stock.StockableProductStore;
import org.cyk.system.company.persistence.api.product.ProductStoreDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.model.store.Store;
import org.cyk.utility.common.helper.LoggingHelper;

public class ProductStoreBusinessImpl extends AbstractTypedBusinessService<ProductStore, ProductStoreDao> implements ProductStoreBusiness,Serializable {
	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public ProductStoreBusinessImpl(ProductStoreDao dao) {
		super(dao);
	}
	
	@Override
	protected void afterCrud(ProductStore productStore, Crud crud) {
		super.afterCrud(productStore, crud);
		if(Crud.isCreateOrUpdate(crud)){
			if(Crud.CREATE.equals(crud)){
				if(Boolean.TRUE.equals(productStore.getProduct().getSalable())){
					inject(SalableProductStoreBusiness.class).create(inject(SalableProductStoreBusiness.class).instanciateOne().setProductStore(productStore));
				}
				if(Boolean.TRUE.equals(productStore.getProduct().getStockable())){
					StockableProductStore stockableProductStore = inject(StockableProductStoreBusiness.class).instanciateOne().setProductStore(productStore)
							.setQuantityMovementCollectionInitialValueFromObject(productStore.getProduct().getStockQuantityMovementCollectionInitialValue());
					inject(StockableProductStoreBusiness.class).computeChanges(stockableProductStore);
					inject(StockableProductStoreBusiness.class).create(stockableProductStore);
				}
			}
		}
	}
	
	@Override
	protected void computeChanges(ProductStore productStore, LoggingHelper.Message.Builder loggingMessageBuilder) {
		super.computeChanges(productStore, loggingMessageBuilder);
	}
	
	@Override
	protected Object[] getPropertyValueTokens(ProductStore productStore, String name) {
		if(ArrayUtils.contains(new String[]{GlobalIdentifier.FIELD_CODE}, name))
			return new Object[]{productStore.getProduct(),productStore.getStore()};
		if(ArrayUtils.contains(new String[]{GlobalIdentifier.FIELD_NAME}, name))
			return new Object[]{productStore.getProduct(),productStore.getStore()};
		return super.getPropertyValueTokens(productStore, name);
	}
	
	@Override
	public ProductStore findByProductByStore(Product product, Store store) {
		return dao.readByProductByStore(product, store);
	}
}
