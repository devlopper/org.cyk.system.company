package org.cyk.system.company.persistence.impl.sale;

import java.util.Collection;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductProperties;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.persistence.impl.AbstractEnumerationDaoImpl;
import org.cyk.system.root.persistence.impl.QueryWrapper;
import org.cyk.utility.common.helper.CollectionHelper;
import org.cyk.utility.common.helper.FilterHelper;
import org.cyk.utility.common.helper.FilterHelper.Filter;
import org.cyk.utility.common.helper.StructuredQueryLanguageHelper.Builder.Adapter.Default.JavaPersistenceQueryLanguage;

public class SalableProductDaoImpl extends AbstractEnumerationDaoImpl<SalableProduct> implements SalableProductDao {
	private static final long serialVersionUID = 6920278182318788380L;

	@Override
	protected void listenInstanciateJpqlBuilder(String name, JavaPersistenceQueryLanguage builder) {
		super.listenInstanciateJpqlBuilder(name, builder);
		if(readByFilter.equals(name)){
			builder.setFieldName(SalableProduct.FIELD_PRODUCT).where().and().in(GlobalIdentifier.FIELD_IDENTIFIER);
			builder.setFieldName(SalableProduct.FIELD_PROPERTIES).where().and().in(GlobalIdentifier.FIELD_IDENTIFIER);
		}
	}
			
	@SuppressWarnings("unchecked")
	@Override
	protected <T> void processQueryWrapper(Class<T> aClass,QueryWrapper<T> queryWrapper, String queryName,Object[] arguments) {
		super.processQueryWrapper(aClass, queryWrapper, queryName,arguments);
		if(ArrayUtils.contains(new String[]{readByFilter,countByFilter}, queryName)){
			FilterHelper.Filter<T> filter = (Filter<T>) arguments[0];
			queryWrapper.parameterInIdentifiers(filter.filterMasters(Product.class),SalableProduct.FIELD_PRODUCT,GlobalIdentifier.FIELD_IDENTIFIER);
			queryWrapper.parameterInIdentifiers(filter.filterMasters(SalableProductProperties.class),SalableProduct.FIELD_PROPERTIES,GlobalIdentifier.FIELD_IDENTIFIER);
		}
	}

	@Override
	public SalableProduct readByProduct(Product product) {
		return CollectionHelper.getInstance().getFirst(readByFilter(new SalableProduct.Filter().addMaster(product)));
	}
	
	@Override
	public Collection<SalableProduct> readByProperties(SalableProductProperties salableProductProperties) {
		return readByFilter(new SalableProduct.Filter().addMaster(salableProductProperties));
	}
	
	@Override
	public Long countByProperties(SalableProductProperties salableProductProperties) {
		return countByFilter(new SalableProduct.Filter().addMaster(salableProductProperties));
	}
}
