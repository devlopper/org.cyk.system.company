package org.cyk.system.company.business.impl.product;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.api.product.ProductStoreBusiness;
import org.cyk.system.company.business.api.stock.StockableProductStoreBusiness;
import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.persistence.api.product.ProductStoreDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;

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
				if(productStore.getProduct() instanceof TangibleProduct){
					if(Boolean.TRUE.equals(productStore.getProduct().getIsStockable())){
						inject(StockableProductStoreBusiness.class).create(inject(StockableProductStoreBusiness.class).instanciateOne().setProductStore(productStore));
					}
				}
			}
		}
	}
	
	@Override
	protected Object[] getPropertyValueTokens(ProductStore productStore, String name) {
		if(ArrayUtils.contains(new String[]{GlobalIdentifier.FIELD_CODE}, name))
			return new Object[]{productStore.getProduct(),productStore.getStore()};
		if(ArrayUtils.contains(new String[]{GlobalIdentifier.FIELD_NAME}, name))
			return new Object[]{productStore.getProduct(),productStore.getStore()};
		return super.getPropertyValueTokens(productStore, name);
	}
	
	
}
