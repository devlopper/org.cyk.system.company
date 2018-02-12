package org.cyk.system.company.business.impl.product;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.persistence.api.product.TangibleProductDao;
import org.cyk.system.root.business.api.mathematics.MovementCollectionBusiness;
import org.cyk.system.root.business.api.mathematics.MovementCollectionIdentifiableGlobalIdentifierBusiness;
import org.cyk.system.root.model.RootConstant;

public class TangibleProductBusinessImpl extends AbstractProductBusinessImpl<TangibleProduct,TangibleProductDao> implements TangibleProductBusiness {
	private static final long serialVersionUID = 2801588592108008404L;

	@Inject
    public TangibleProductBusinessImpl(TangibleProductDao dao) {
        super(dao);
    }
	
	@Override
	protected void afterCreate(TangibleProduct tangibleProduct) {
		super.afterCreate(tangibleProduct);
		if(tangibleProduct.getQuantityMovementCollection() == null){
			tangibleProduct.setQuantityMovementCollection(inject(MovementCollectionBusiness.class).instanciateOne(RootConstant.Code.MovementCollectionType.STOCK_REGISTER
				,tangibleProduct.getQuantity(),tangibleProduct));
			inject(MovementCollectionIdentifiableGlobalIdentifierBusiness.class).create(tangibleProduct.getQuantityMovementCollection(), tangibleProduct);
		}
	}
	
	/**/
	
	public static class BuilderOneDimensionArray extends AbstractProductBusinessImpl.BuilderOneDimensionArray<TangibleProduct> implements Serializable {
		private static final long serialVersionUID = 1L;

		public BuilderOneDimensionArray() {
			super(TangibleProduct.class);
		}
		
	}	
}
