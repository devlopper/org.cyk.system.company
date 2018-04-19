package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;

import org.cyk.system.company.business.api.product.ProductStoreBusiness;
import org.cyk.system.company.business.api.stock.StockableProductStoreBusiness;
import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.model.stock.StockableProductStore;
import org.cyk.system.company.persistence.api.stock.StockableProductStoreDao;
import org.cyk.system.root.business.api.mathematics.movement.MovementCollectionIdentifiableGlobalIdentifierBusiness;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.mathematics.movement.MovementCollection;
import org.cyk.system.root.model.mathematics.movement.MovementCollectionIdentifiableGlobalIdentifier;
import org.cyk.ui.web.primefaces.mathematics.movement.MovementIdentifiableEditPageFormMaster.PrepareMovementCollectionValuesTransferItemCollectionListener;
import org.cyk.utility.common.helper.CollectionHelper;

public class PrepareMovementCollectionValuesTransferItemCollectionAdapter extends PrepareMovementCollectionValuesTransferItemCollectionListener.Adapter.Default implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public AbstractIdentifiable getSourceIdentifiableJoined(EndPoint sender, EndPoint receiver,MovementCollection source,MovementCollectionIdentifiableGlobalIdentifier movementCollectionIdentifiableGlobalIdentifier) {
		if(movementCollectionIdentifiableGlobalIdentifier!=null){
			return inject(StockableProductStoreDao.class).readByGlobalIdentifier(movementCollectionIdentifiableGlobalIdentifier.getIdentifiableGlobalIdentifier());
		}
		return super.getSourceIdentifiableJoined(sender, receiver, source,movementCollectionIdentifiableGlobalIdentifier);
	}
	
	@Override
	public MovementCollection getDestinationMovementCollection(EndPoint sender,EndPoint receiver,MovementCollection source,AbstractIdentifiable sourceIdentifiableJoined) {
		if(sender!=null && receiver!=null && source!=null){
			if(sourceIdentifiableJoined!=null){
				ProductStore receiverProductStore = inject(ProductStoreBusiness.class).findByProductByStore(((StockableProductStore) sourceIdentifiableJoined).getProductStore()
						.getProduct(), receiver.getStore());
				StockableProductStore receiverStockableProductStore = inject(StockableProductStoreBusiness.class).findByProductStore(receiverProductStore);
				MovementCollection receiverMovementCollection = CollectionHelper.getInstance().getFirst(inject(MovementCollectionIdentifiableGlobalIdentifierBusiness.class).findByIdentifiableGlobalIdentifier(receiverStockableProductStore)).getMovementCollection();
				return receiverMovementCollection; 
				
			}
			
		}
		return super.getDestinationMovementCollection(sender,receiver, source,sourceIdentifiableJoined);
	}
	
}
