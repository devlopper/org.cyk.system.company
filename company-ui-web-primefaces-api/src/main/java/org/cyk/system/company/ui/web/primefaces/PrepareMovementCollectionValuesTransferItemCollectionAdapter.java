package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;

import org.cyk.system.company.business.api.product.ProductStoreBusiness;
import org.cyk.system.company.business.api.stock.StockableProductStoreBusiness;
import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.model.stock.StockableProductStore;
import org.cyk.system.company.persistence.api.stock.StockableProductStoreDao;
import org.cyk.system.root.business.api.mathematics.movement.MovementCollectionIdentifiableGlobalIdentifierBusiness;
import org.cyk.system.root.business.api.party.BusinessRoleBusiness;
import org.cyk.system.root.business.api.party.PartyIdentifiableGlobalIdentifierBusiness;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.mathematics.movement.MovementCollection;
import org.cyk.system.root.model.mathematics.movement.MovementCollectionIdentifiableGlobalIdentifier;
import org.cyk.system.root.model.party.Party;
import org.cyk.system.root.model.party.PartyIdentifiableGlobalIdentifier;
import org.cyk.system.root.model.store.Store;
import org.cyk.system.root.persistence.api.store.StoreDao;
import org.cyk.ui.web.primefaces.mathematics.movement.MovementIdentifiableEditPageFormMaster.PrepareMovementCollectionValuesTransferItemCollectionListener;
import org.cyk.utility.common.helper.CollectionHelper;

public class PrepareMovementCollectionValuesTransferItemCollectionAdapter extends PrepareMovementCollectionValuesTransferItemCollectionListener.Adapter.Default implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public MovementCollection getDestinationMovementCollection(Party sender,Party receiver,MovementCollection source) {
		if(sender!=null && receiver!=null && source!=null){
			PartyIdentifiableGlobalIdentifier partyIdentifiableGlobalIdentifier = CollectionHelper.getInstance().getFirst(
					inject(PartyIdentifiableGlobalIdentifierBusiness.class).findByPartyByBusinessRole(receiver, inject(BusinessRoleBusiness.class)
							.find(RootConstant.Code.BusinessRole.COMPANY)));
			Store store = inject(StoreDao.class).readByGlobalIdentifier(partyIdentifiableGlobalIdentifier.getIdentifiableGlobalIdentifier());
			
			MovementCollectionIdentifiableGlobalIdentifier movementCollectionIdentifiableGlobalIdentifier = CollectionHelper.getInstance().getFirst(
					inject(MovementCollectionIdentifiableGlobalIdentifierBusiness.class).findByMovementCollection(source));
			if(movementCollectionIdentifiableGlobalIdentifier!=null){
				StockableProductStore stockableProductStore = inject(StockableProductStoreDao.class).readByGlobalIdentifier(movementCollectionIdentifiableGlobalIdentifier
						.getIdentifiableGlobalIdentifier());
				if(stockableProductStore!=null){
					ProductStore productStore = inject(ProductStoreBusiness.class).findByProductByStore(stockableProductStore.getProductStore().getProduct()
							, store);
					StockableProductStore r = inject(StockableProductStoreBusiness.class).findByProductStore(productStore);
					MovementCollection d = CollectionHelper.getInstance().getFirst(inject(MovementCollectionIdentifiableGlobalIdentifierBusiness.class).findByIdentifiableGlobalIdentifier(r)).getMovementCollection();
					return d;
				}
			}
			
		}
		return super.getDestinationMovementCollection(sender,receiver, source);
	}
	
}
