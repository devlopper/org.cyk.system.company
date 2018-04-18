package org.cyk.system.company.business.impl.stock;

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.business.api.stock.StockableProductStoresTransferBusiness;
import org.cyk.system.company.model.stock.StockableProductStoresTransfer;
import org.cyk.system.company.persistence.api.stock.StockableProductStoresTransferDao;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.api.mathematics.movement.MovementCollectionValuesTransferBusiness;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.business.impl.helper.FieldHelper;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.model.party.BusinessRole;
import org.cyk.system.root.model.party.PartyIdentifiableGlobalIdentifier;
import org.cyk.system.root.persistence.api.party.PartyIdentifiableGlobalIdentifierDao;
import org.cyk.utility.common.helper.CollectionHelper;
import org.cyk.utility.common.helper.LoggingHelper;

public class StockableProductStoresTransferBusinessImpl extends AbstractTypedBusinessService<StockableProductStoresTransfer, StockableProductStoresTransferDao> implements StockableProductStoresTransferBusiness,Serializable {
	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public StockableProductStoresTransferBusinessImpl(StockableProductStoresTransferDao dao) {
		super(dao);
	}
	
	@Override
	public StockableProductStoresTransfer instanciateOne() {
		return super.instanciateOne().setMovementCollectionValuesTransfer(inject(MovementCollectionValuesTransferBusiness.class).instanciateOne());
	}
	
	@Override
	public Collection<String> findRelatedInstanceFieldNames(StockableProductStoresTransfer identifiable) {
		return CollectionHelper.getInstance().add(super.findRelatedInstanceFieldNames(identifiable), Boolean.TRUE, StockableProductStoresTransfer.FIELD_MOVEMENT_COLLECTION_VALUES_TRANSFER);
	}
	
	@Override
	protected void beforeCrud(StockableProductStoresTransfer stockableProductStoresTransfer, Crud crud) {
		super.beforeCrud(stockableProductStoresTransfer, crud);
		if(stockableProductStoresTransfer.getSender()!=null) {
			PartyIdentifiableGlobalIdentifier partyIdentifiableGlobalIdentifier = CollectionHelper.getInstance().getFirst(inject(PartyIdentifiableGlobalIdentifierDao.class)
					.readByIdentifiableGlobalIdentifierByRole(stockableProductStoresTransfer.getSender().getGlobalIdentifier()
					,read(BusinessRole.class, RootConstant.Code.BusinessRole.COMPANY)));
			if(partyIdentifiableGlobalIdentifier!=null)
				stockableProductStoresTransfer.getMovementCollectionValuesTransfer().setSender(partyIdentifiableGlobalIdentifier.getParty());
		}
		
		if(stockableProductStoresTransfer.getReceiver()!=null) {
			PartyIdentifiableGlobalIdentifier partyIdentifiableGlobalIdentifier = CollectionHelper.getInstance().getFirst(inject(PartyIdentifiableGlobalIdentifierDao.class)
					.readByIdentifiableGlobalIdentifierByRole(stockableProductStoresTransfer.getReceiver().getGlobalIdentifier()
					,read(BusinessRole.class, RootConstant.Code.BusinessRole.COMPANY)));
			if(partyIdentifiableGlobalIdentifier!=null)
				stockableProductStoresTransfer.getMovementCollectionValuesTransfer().setReceiver(partyIdentifiableGlobalIdentifier.getParty());
		}
	}
	
	@Override
	protected void computeChanges(StockableProductStoresTransfer stockableProductStoresTransfer, LoggingHelper.Message.Builder loggingMessageBuilder) {
		super.computeChanges(stockableProductStoresTransfer, loggingMessageBuilder);
		if(inject(MovementCollectionValuesTransferBusiness.class).isNotIdentified(stockableProductStoresTransfer.getMovementCollectionValuesTransfer()))
			FieldHelper.getInstance().copy(stockableProductStoresTransfer, stockableProductStoresTransfer.getMovementCollectionValuesTransfer(),Boolean.FALSE
				,org.cyk.utility.common.helper.FieldHelper.getInstance().buildPath(
						AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_CODE),org.cyk.utility.common.helper.FieldHelper.getInstance().buildPath(
								AbstractIdentifiable.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_NAME));
	}
	
}
