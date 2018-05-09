package org.cyk.system.company.persistence.impl.product;

import java.util.Collection;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.persistence.api.product.ProductStoreDao;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.model.party.Store;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;
import org.cyk.system.root.persistence.impl.QueryWrapper;
import org.cyk.utility.common.helper.CollectionHelper;
import org.cyk.utility.common.helper.FilterHelper;
import org.cyk.utility.common.helper.FilterHelper.Filter;
import org.cyk.utility.common.helper.StructuredQueryLanguageHelper.Builder.Adapter.Default.JavaPersistenceQueryLanguage;

public class ProductStoreDaoImpl extends AbstractTypedDao<ProductStore> implements ProductStoreDao {
	private static final long serialVersionUID = 6920278182318788380L;

	@Override
	public ProductStore readByProductByStore(Product product, Store store) {
		if(product == null || store == null)
			return null;
		return CollectionHelper.getInstance().getFirst(readByFilter(new ProductStore.Filter().addMaster(product).addMaster(store)));
	}
	
	@Override
	public ProductStore readByProductCodeByStoreCode(String productCode, String storeCode) {
		return readByProductByStore(read(Product.class,productCode),read(Store.class,storeCode));
	}
	
	@Override
	public Collection<ProductStore> readByStore(Store store) {
		if(store == null)
			return null;
		return readByFilter(new ProductStore.Filter().addMaster(store));
	}
	
	@Override
	protected void listenInstanciateJpqlBuilder(String name, JavaPersistenceQueryLanguage builder) {
		super.listenInstanciateJpqlBuilder(name, builder);
		if(readByFilter.equals(name)){
			builder.setFieldName(ProductStore.FIELD_PRODUCT).where().and().in(GlobalIdentifier.FIELD_IDENTIFIER);
			builder.setFieldName(ProductStore.FIELD_STORE).where().and().in(GlobalIdentifier.FIELD_IDENTIFIER);
		}
	}
			
	@SuppressWarnings("unchecked")
	@Override
	protected <T> void processQueryWrapper(Class<T> aClass,QueryWrapper<T> queryWrapper, String queryName,Object[] arguments) {
		super.processQueryWrapper(aClass, queryWrapper, queryName,arguments);
		if(ArrayUtils.contains(new String[]{readByFilter,countByFilter}, queryName)){
			FilterHelper.Filter<T> filter = (Filter<T>) arguments[0];
			queryWrapper.parameterInIdentifiers(filter.filterMasters(Product.class),ProductStore.FIELD_PRODUCT,GlobalIdentifier.FIELD_IDENTIFIER);
			queryWrapper.parameterInIdentifiers(filter.filterMasters(Store.class),ProductStore.FIELD_STORE,GlobalIdentifier.FIELD_IDENTIFIER);
		}
	}

	
	
}
