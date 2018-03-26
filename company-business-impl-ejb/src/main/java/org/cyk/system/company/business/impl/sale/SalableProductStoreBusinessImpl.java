package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SalableProductStoreBusiness;
import org.cyk.system.company.model.product.SalableProductStore;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.persistence.api.sale.SalableProductStoreDao;
import org.cyk.system.root.business.impl.AbstractCollectionBusinessImpl;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

public class SalableProductStoreBusinessImpl extends AbstractTypedBusinessService<SalableProductStore, SalableProductStoreDao> implements SalableProductStoreBusiness,Serializable {
	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public SalableProductStoreBusinessImpl(SalableProductStoreDao dao) {
		super(dao);
	}
	
	/**/
	
	public static class BuilderOneDimensionArray extends AbstractCollectionBusinessImpl.BuilderOneDimensionArray<SalableProduct> implements Serializable {
		private static final long serialVersionUID = 1L;

		public BuilderOneDimensionArray() {
			super(SalableProduct.class);
		}
		
	}

}