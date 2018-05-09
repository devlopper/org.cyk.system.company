package org.cyk.system.company.business.impl.stock;

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.business.api.stock.StockableProductStoreBusiness;
import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.model.stock.StockableProductStore;
import org.cyk.system.company.persistence.api.stock.StockableProductStoreDao;
import org.cyk.system.root.business.api.mathematics.movement.MovementCollectionBusiness;
import org.cyk.system.root.business.api.mathematics.movement.MovementCollectionIdentifiableGlobalIdentifierBusiness;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.business.impl.helper.FieldHelper;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.model.mathematics.movement.MovementCollection;
import org.cyk.system.root.model.party.Party;
import org.cyk.system.root.persistence.api.mathematics.movement.MovementCollectionDao;
import org.cyk.system.root.persistence.api.mathematics.movement.MovementCollectionTypeDao;
import org.cyk.system.root.persistence.api.party.PartyDao;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.helper.CollectionHelper;
import org.cyk.utility.common.helper.LoggingHelper.Message.Builder;
import org.cyk.utility.common.helper.StringHelper;

public class StockableProductStoreBusinessImpl extends AbstractTypedBusinessService<StockableProductStore, StockableProductStoreDao> implements StockableProductStoreBusiness,Serializable {
	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public StockableProductStoreBusinessImpl(StockableProductStoreDao dao) {
		super(dao);
	}
	
	@Override
	public StockableProductStore instanciateOne() {
		StockableProductStore stockableProductStore = super.instanciateOne();
		stockableProductStore.setQuantityMovementCollection(inject(MovementCollectionBusiness.class).instanciateOne()
				.setTypeFromCode(RootConstant.Code.MovementCollectionType.STOCK_REGISTER).setIsCreateBufferAutomatically(Boolean.TRUE));
		return stockableProductStore;
	}
	
	@Override
	public StockableProductStore findByProductStore(ProductStore productStore) {
		return dao.readByProductStore(productStore);
	}
	
	@Override
	protected void computeChanges(StockableProductStore stockableProductStore, Builder logMessageBuilder) {
		super.computeChanges(stockableProductStore, logMessageBuilder);
		FieldHelper.getInstance().copy(stockableProductStore.getProductStore(), stockableProductStore,Boolean.FALSE
				,org.cyk.utility.common.helper.FieldHelper.getInstance().buildPath(
						AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_CODE),org.cyk.utility.common.helper.FieldHelper.getInstance().buildPath(
								AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME));
		if(stockableProductStore.getQuantityMovementCollection()!=null){
			MovementCollection movementCollection = stockableProductStore.getQuantityMovementCollection();
			if(StringHelper.getInstance().isBlank(movementCollection.getCode()) &&  StringHelper.getInstance().isNotBlank(stockableProductStore.getCode()))
				movementCollection.setCode(RootConstant.Code.generate(stockableProductStore.getCode(),movementCollection.getType().getCode()));
			if(StringHelper.getInstance().isBlank(movementCollection.getName()) && StringHelper.getInstance().isNotBlank(stockableProductStore.getName()))
				movementCollection.setName(stockableProductStore.getName()+Constant.CHARACTER_VERTICAL_BAR+movementCollection.getType().getName());
		}
	}
	
	@Override
	protected void afterCreate(StockableProductStore stockableProductStore) {
		super.afterCreate(stockableProductStore);
		if(stockableProductStore.getQuantityMovementCollection() != null){
			stockableProductStore.getQuantityMovementCollection().setValue(stockableProductStore.getQuantityMovementCollectionInitialValue());
			inject(MovementCollectionIdentifiableGlobalIdentifierBusiness.class).create(stockableProductStore.getQuantityMovementCollection(), stockableProductStore);
			
			Party party = inject(PartyDao.class).readFirstByIdentifiableByBusinessRoleCode(stockableProductStore.getProductStore().getStore()
					, RootConstant.Code.BusinessRole.COMPANY);
			/*
			 * Join to functionalities related to party
			 */
			if(party!=null){
				inject(MovementCollectionIdentifiableGlobalIdentifierBusiness.class).create(stockableProductStore.getQuantityMovementCollection(), party);	
			}
		}
	}
	
	@Override
	public void setQuantityMovementCollection(StockableProductStore stockableProductStore) {
		Collection<MovementCollection> movementCollections = inject(MovementCollectionDao.class).readByTypeByJoin(
				inject(MovementCollectionTypeDao.class).read(RootConstant.Code.MovementCollectionType.STOCK_REGISTER), stockableProductStore);
		stockableProductStore.setQuantityMovementCollection(CollectionHelper.getInstance().getFirst(movementCollections));
	}

	
}
