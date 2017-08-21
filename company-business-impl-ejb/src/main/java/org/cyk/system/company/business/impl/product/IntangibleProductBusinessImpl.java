package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.IntangibleProductBusiness;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.persistence.api.product.IntangibleProductDao;

public class IntangibleProductBusinessImpl extends AbstractProductBusinessImpl<IntangibleProduct,IntangibleProductDao> implements IntangibleProductBusiness {
	
	private static final long serialVersionUID = 2801588592108008404L;

	@Inject
    public IntangibleProductBusinessImpl(IntangibleProductDao dao) {
        super(dao);
    }
	
	@Override
	protected Set<IntangibleProduct> products(Collection<SalableProductCollectionItem> saleProducts) {
	
		return null;
	}



	@Override
	protected void beforeUpdate(IntangibleProduct product, BigDecimal usedCount) {
			
	}
	
	/**/
	
	public static class BuilderOneDimensionArray extends AbstractProductBusinessImpl.BuilderOneDimensionArray<IntangibleProduct> implements Serializable {
		private static final long serialVersionUID = 1L;

		public BuilderOneDimensionArray() {
			super(IntangibleProduct.class);
		}
		
	}	

}
