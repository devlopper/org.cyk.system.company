package org.cyk.system.company.business.impl.stock;

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.business.api.stock.StockableProductBusiness;
import org.cyk.system.company.model.stock.StockableProduct;
import org.cyk.system.company.persistence.api.stock.StockableProductDao;
import org.cyk.system.root.business.api.mathematics.MovementCollectionBusiness;
import org.cyk.system.root.business.api.mathematics.MovementCollectionIdentifiableGlobalIdentifierBusiness;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.business.impl.helper.FieldHelper;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.system.root.persistence.api.mathematics.MovementCollectionDao;
import org.cyk.system.root.persistence.api.mathematics.MovementCollectionTypeDao;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.helper.CollectionHelper;
import org.cyk.utility.common.helper.LoggingHelper.Message.Builder;
import org.cyk.utility.common.helper.StringHelper;

public class StockableProductBusinessImpl extends AbstractTypedBusinessService<StockableProduct, StockableProductDao> implements StockableProductBusiness,Serializable {
	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public StockableProductBusinessImpl(StockableProductDao dao) {
		super(dao);
	}
	
	@Override
	public StockableProduct instanciateOne() {
		StockableProduct stockableProduct = super.instanciateOne();
		stockableProduct.setQuantityMovementCollection(inject(MovementCollectionBusiness.class).instanciateOne()
				.setTypeFromCode(RootConstant.Code.MovementCollectionType.STOCK_REGISTER));
		return stockableProduct;
	}
	
	@Override
	protected void computeChanges(StockableProduct stockableProduct, Builder logMessageBuilder) {
		super.computeChanges(stockableProduct, logMessageBuilder);
		FieldHelper.getInstance().copy(stockableProduct.getProduct(), stockableProduct,Boolean.FALSE
				,org.cyk.utility.common.helper.FieldHelper.getInstance().buildPath(
						AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_CODE),org.cyk.utility.common.helper.FieldHelper.getInstance().buildPath(
								AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME));
		if(stockableProduct.getQuantityMovementCollection()!=null){
			MovementCollection movementCollection = stockableProduct.getQuantityMovementCollection();
			if(StringHelper.getInstance().isBlank(movementCollection.getCode()) &&  StringHelper.getInstance().isNotBlank(stockableProduct.getCode()))
				movementCollection.setCode(RootConstant.Code.generate(stockableProduct.getCode(),movementCollection.getType().getCode()));
			if(StringHelper.getInstance().isBlank(movementCollection.getName()) && StringHelper.getInstance().isNotBlank(stockableProduct.getName()))
				movementCollection.setName(stockableProduct.getName()+Constant.CHARACTER_VERTICAL_BAR+movementCollection.getType().getName());
			movementCollection.setInitialValue(stockableProduct.getQuantityMovementCollectionInitialValue());
		}
	}
	
	@Override
	protected void afterCreate(StockableProduct stockableProduct) {
		super.afterCreate(stockableProduct);
		if(stockableProduct.getQuantityMovementCollection() != null){
			stockableProduct.getQuantityMovementCollection().setValue(stockableProduct.getQuantityMovementCollectionInitialValue());
			inject(MovementCollectionIdentifiableGlobalIdentifierBusiness.class).create(stockableProduct.getQuantityMovementCollection(), stockableProduct);
		}
	}
	
	@Override
	public void setQuantityMovementCollection(StockableProduct stockableProduct) {
		Collection<MovementCollection> movementCollections = inject(MovementCollectionDao.class).readByTypeByJoin(
				inject(MovementCollectionTypeDao.class).read(RootConstant.Code.MovementCollectionType.STOCK_REGISTER), stockableProduct);
		stockableProduct.setQuantityMovementCollection(CollectionHelper.getInstance().getFirst(movementCollections));
	}
}
