package org.cyk.system.company.persistence.impl.stock;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.model.stock.StockableProductStore;
import org.cyk.system.company.persistence.api.stock.StockableProductStoreDao;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.model.party.Store;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;
import org.cyk.system.root.persistence.impl.QueryWrapper;
import org.cyk.utility.common.helper.CollectionHelper;
import org.cyk.utility.common.helper.FieldHelper;
import org.cyk.utility.common.helper.FilterHelper;
import org.cyk.utility.common.helper.FilterHelper.Filter;
import org.cyk.utility.common.helper.StructuredQueryLanguageHelper.Builder.Adapter.Default.JavaPersistenceQueryLanguage;

public class StockableProductStoreDaoImpl extends AbstractTypedDao<StockableProductStore> implements StockableProductStoreDao {
	private static final long serialVersionUID = 6920278182318788380L;
	
	@Override
	public Collection<StockableProductStore> readByProductStores(Collection<ProductStore> productStores) {
		return readByFilter(new StockableProductStore.Filter().addMasters(productStores));
	}
	
	@Override
	public Collection<StockableProductStore> readByProductStores(ProductStore...productStores) {
		return readByProductStores(Arrays.asList(productStores));
	}
	
	@Override
	public StockableProductStore readByProductStore(ProductStore productStore) {
		if(productStore == null)
			return null;
		return CollectionHelper.getInstance().getFirst(readByProductStores(productStore));
	}
	
	@Override
	public Collection<StockableProductStore> readByStores(Collection<Store> stores) {
		return readByFilter(new StockableProductStore.Filter().addMasters(stores));
	}
	
	@Override
	public Collection<StockableProductStore> readByStores(Store... stores) {
		return readByStores(Arrays.asList(stores));
	}
	
	@Override
	public Collection<StockableProductStore> readByStoreCodes(String... storeCodes) {
		return readByStores(readMany(Store.class,storeCodes));
	}
	
	@Override
	protected void listenInstanciateJpqlBuilder(String name, JavaPersistenceQueryLanguage builder) {
		super.listenInstanciateJpqlBuilder(name, builder);
		if(readByFilter.equals(name)){
			builder.setFieldName(StockableProductStore.FIELD_PRODUCT_STORE).where().and().in(GlobalIdentifier.FIELD_IDENTIFIER);
			builder.setFieldName(FieldHelper.getInstance().buildPath(StockableProductStore.FIELD_PRODUCT_STORE,ProductStore.FIELD_STORE)).where().and().in(GlobalIdentifier.FIELD_IDENTIFIER);
			
		}
	}
			
	@SuppressWarnings("unchecked")
	@Override
	protected <T> void processQueryWrapper(Class<T> aClass,QueryWrapper<T> queryWrapper, String queryName,Object[] arguments) {
		super.processQueryWrapper(aClass, queryWrapper, queryName,arguments);
		if(ArrayUtils.contains(new String[]{readByFilter,countByFilter}, queryName)){
			FilterHelper.Filter<T> filter = (Filter<T>) arguments[0];
			queryWrapper.parameterInIdentifiers(filter.filterMasters(ProductStore.class),StockableProductStore.FIELD_PRODUCT_STORE,GlobalIdentifier.FIELD_IDENTIFIER);
			queryWrapper.parameterInIdentifiers(filter.filterMasters(Store.class),StockableProductStore.FIELD_PRODUCT_STORE,ProductStore.FIELD_STORE,GlobalIdentifier.FIELD_IDENTIFIER);
		}
	}
}
