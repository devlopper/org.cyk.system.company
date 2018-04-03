package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SalableProductCollectionPropertiesTypeBusiness;
import org.cyk.system.company.model.sale.SalableProductCollectionPropertiesType;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionPropertiesTypeDao;
import org.cyk.system.root.business.impl.pattern.tree.AbstractDataTreeTypeBusinessImpl;

public class SalableProductCollectionPropertiesTypeBusinessImpl extends AbstractDataTreeTypeBusinessImpl<SalableProductCollectionPropertiesType,SalableProductCollectionPropertiesTypeDao> implements SalableProductCollectionPropertiesTypeBusiness {
	private static final long serialVersionUID = -5970296090669949506L;

	@Inject
    public SalableProductCollectionPropertiesTypeBusinessImpl(SalableProductCollectionPropertiesTypeDao dao) {
        super(dao);
    }
	
	public static class BuilderOneDimensionArray extends AbstractDataTreeTypeBusinessImpl.BuilderOneDimensionArray<SalableProductCollectionPropertiesType> implements Serializable {
		private static final long serialVersionUID = 1L;

		public BuilderOneDimensionArray() {
			super(SalableProductCollectionPropertiesType.class);
			addParameterArrayElementStringIndexInstance(2,SalableProductCollectionPropertiesType.FIELD_BALANCE_MOVEMENT_COLLECTION_UPDATABLE
					,3,SalableProductCollectionPropertiesType.FIELD_STOCK_MOVEMENT_COLLECTION_UPDATABLE);
		}
		
	}
}
