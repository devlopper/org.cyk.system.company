package org.cyk.system.company.business.impl.stock;

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.business.api.stock.StockableTangibleProductBusiness;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.company.persistence.api.stock.StockableTangibleProductDao;
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

public class StockableTangibleProductBusinessImpl extends AbstractTypedBusinessService<StockableTangibleProduct, StockableTangibleProductDao> implements StockableTangibleProductBusiness,Serializable {
	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public StockableTangibleProductBusinessImpl(StockableTangibleProductDao dao) {
		super(dao);
	}
	
	@Override
	public StockableTangibleProduct instanciateOne() {
		StockableTangibleProduct stockableTangibleProduct = super.instanciateOne();
		stockableTangibleProduct.setQuantityMovementCollection(inject(MovementCollectionBusiness.class).instanciateOne(RootConstant.Code.MovementCollectionType.STOCK_REGISTER
				,null,stockableTangibleProduct));
		return stockableTangibleProduct;
	}
	
	@Override
	protected void computeChanges(StockableTangibleProduct stockableTangibleProduct, Builder logMessageBuilder) {
		super.computeChanges(stockableTangibleProduct, logMessageBuilder);
		FieldHelper.getInstance().copy(stockableTangibleProduct.getTangibleProduct(), stockableTangibleProduct,Boolean.FALSE
				,org.cyk.utility.common.helper.FieldHelper.getInstance().buildPath(
						AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_CODE),org.cyk.utility.common.helper.FieldHelper.getInstance().buildPath(
								AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME));
		if(stockableTangibleProduct.getQuantityMovementCollection()!=null){
			MovementCollection movementCollection = stockableTangibleProduct.getQuantityMovementCollection();
			if(StringHelper.getInstance().isBlank(movementCollection.getCode()) &&  StringHelper.getInstance().isNotBlank(stockableTangibleProduct.getCode()))
				movementCollection.setCode(RootConstant.Code.generate(stockableTangibleProduct.getCode(),movementCollection.getType().getCode()));
			if(StringHelper.getInstance().isBlank(movementCollection.getName()) && StringHelper.getInstance().isNotBlank(stockableTangibleProduct.getName()))
				movementCollection.setName(stockableTangibleProduct.getName()+Constant.CHARACTER_VERTICAL_BAR+movementCollection.getType().getName());
		}
	}
	
	@Override
	protected void afterCreate(StockableTangibleProduct stockableTangibleProduct) {
		super.afterCreate(stockableTangibleProduct);
		if(stockableTangibleProduct.getQuantityMovementCollection() != null){
			inject(MovementCollectionIdentifiableGlobalIdentifierBusiness.class).create(stockableTangibleProduct.getQuantityMovementCollection(), stockableTangibleProduct);
		}
	}
	
	@Override
	public void setQuantityMovementCollection(StockableTangibleProduct stockableTangibleProduct) {
		Collection<MovementCollection> movementCollections = inject(MovementCollectionDao.class).readByTypeByJoin(
				inject(MovementCollectionTypeDao.class).read(RootConstant.Code.MovementCollectionType.STOCK_REGISTER), stockableTangibleProduct);
		stockableTangibleProduct.setQuantityMovementCollection(CollectionHelper.getInstance().getFirst(movementCollections));
	}
}
