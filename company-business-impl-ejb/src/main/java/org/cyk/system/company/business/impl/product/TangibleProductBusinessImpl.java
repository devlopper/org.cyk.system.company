package org.cyk.system.company.business.impl.product;

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.persistence.api.product.TangibleProductDao;
import org.cyk.system.root.business.api.mathematics.MovementCollectionBusiness;
import org.cyk.system.root.business.api.mathematics.MovementCollectionIdentifiableGlobalIdentifierBusiness;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.system.root.persistence.api.mathematics.MovementCollectionDao;
import org.cyk.system.root.persistence.api.mathematics.MovementCollectionTypeDao;
import org.cyk.utility.common.helper.CollectionHelper;

public class TangibleProductBusinessImpl extends AbstractProductBusinessImpl<TangibleProduct,TangibleProductDao> implements TangibleProductBusiness {
	private static final long serialVersionUID = 2801588592108008404L;

	@Inject
    public TangibleProductBusinessImpl(TangibleProductDao dao) {
        super(dao);
    }
	
	@Override
	public TangibleProduct instanciateOne() {
		TangibleProduct tangibleProduct = super.instanciateOne();
		tangibleProduct.setQuantityMovementCollection(inject(MovementCollectionBusiness.class).instanciateOne(RootConstant.Code.MovementCollectionType.STOCK_REGISTER
				,null,tangibleProduct));
		return tangibleProduct;
	}
	
	@Override
	public TangibleProduct instanciateOne(String code, String name) {
		TangibleProduct tangibleProduct = super.instanciateOne(code, name);
		tangibleProduct.setQuantityMovementCollection(inject(MovementCollectionBusiness.class).instanciateOne(RootConstant.Code.MovementCollectionType.STOCK_REGISTER
				,null,tangibleProduct));
		return tangibleProduct;
	}
	
	@Override
	protected void afterCreate(TangibleProduct tangibleProduct) {
		super.afterCreate(tangibleProduct);
		if(tangibleProduct.getQuantityMovementCollection() != null){
			inject(MovementCollectionIdentifiableGlobalIdentifierBusiness.class).create(tangibleProduct.getQuantityMovementCollection(), tangibleProduct);
		}
		
	}
	
	@Override
	public void setQuantityMovementCollection(TangibleProduct tangibleProduct) {
		Collection<MovementCollection> movementCollections = inject(MovementCollectionDao.class).readByTypeByJoin(
				inject(MovementCollectionTypeDao.class).read(RootConstant.Code.MovementCollectionType.STOCK_REGISTER), tangibleProduct);
		tangibleProduct.setQuantityMovementCollection(CollectionHelper.getInstance().getFirst(movementCollections));
	}
	
	/**/
	
	public static class BuilderOneDimensionArray extends AbstractProductBusinessImpl.BuilderOneDimensionArray<TangibleProduct> implements Serializable {
		private static final long serialVersionUID = 1L;

		public BuilderOneDimensionArray() {
			super(TangibleProduct.class);
		}
		
	}	
}
