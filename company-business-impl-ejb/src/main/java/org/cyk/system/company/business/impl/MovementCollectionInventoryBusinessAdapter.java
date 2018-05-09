package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.model.stock.StockableProductStore;
import org.cyk.system.company.persistence.api.product.ProductStoreDao;
import org.cyk.system.company.persistence.api.stock.StockableProductStoreDao;
import org.cyk.system.root.business.impl.mathematics.movement.MovementCollectionInventoryBusinessImpl;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.mathematics.movement.MovementCollection;
import org.cyk.system.root.model.mathematics.movement.MovementCollectionIdentifiableGlobalIdentifier;
import org.cyk.system.root.model.party.Party;
import org.cyk.system.root.model.party.PartyIdentifiableGlobalIdentifier;
import org.cyk.system.root.persistence.api.mathematics.movement.MovementCollectionIdentifiableGlobalIdentifierDao;
import org.cyk.system.root.persistence.api.party.BusinessRoleDao;
import org.cyk.system.root.persistence.api.party.PartyIdentifiableGlobalIdentifierDao;
import org.cyk.system.root.persistence.api.party.StoreDao;
import org.cyk.system.root.persistence.impl.Utils;
import org.cyk.utility.common.helper.CollectionHelper;
import org.cyk.utility.common.helper.MethodHelper;

public class MovementCollectionInventoryBusinessAdapter extends MovementCollectionInventoryBusinessImpl.Listener.Adapter.Default implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Collection<MovementCollection> findMovementCollectionByParty(Party party) {
		PartyIdentifiableGlobalIdentifier partyIdentifiableGlobalIdentifier = CollectionHelper.getInstance().getFirst(inject(PartyIdentifiableGlobalIdentifierDao.class).readByPartyByBusinessRole(party, inject(BusinessRoleDao.class)
				.read(RootConstant.Code.BusinessRole.COMPANY)));
		if(partyIdentifiableGlobalIdentifier==null)
			return super.findMovementCollectionByParty(party);
		Collection<ProductStore> productStores = inject(ProductStoreDao.class).readByStore(inject(StoreDao.class).read(partyIdentifiableGlobalIdentifier.getIdentifiableGlobalIdentifier().getCode()));
		Collection<StockableProductStore> stockableProductStores = inject(StockableProductStoreDao.class).readByProductStores(productStores);
		return MethodHelper.getInstance().callGet(inject(MovementCollectionIdentifiableGlobalIdentifierDao.class).readByIdentifiableGlobalIdentifiers(Utils
				.getGlobalIdentfiers(stockableProductStores)),MovementCollection.class,MovementCollectionIdentifiableGlobalIdentifier.FIELD_MOVEMENT_COLLECTION);
	}
	
}
