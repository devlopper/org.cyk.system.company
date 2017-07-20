package org.cyk.system.company.persistence.impl.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.SalableProductCollectionItemSaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemSaleCashRegisterMovementDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class SalableProductCollectionItemSaleCashRegisterMovementDaoImpl extends AbstractTypedDao<SalableProductCollectionItemSaleCashRegisterMovement> implements SalableProductCollectionItemSaleCashRegisterMovementDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readBySalableProductCollectionItem,readBySaleCashRegisterMovement;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readBySalableProductCollectionItem, _select().where(SalableProductCollectionItemSaleCashRegisterMovement.FIELD_SALABLE_PRODUCT_COLLECTION_ITEM));
		registerNamedQuery(readBySaleCashRegisterMovement, _select().where(SalableProductCollectionItemSaleCashRegisterMovement.FIELD_SALE_CASH_REGISTER_MOVEMENT));
	}

	@Override
	public Collection<SalableProductCollectionItemSaleCashRegisterMovement> readBySalableProductCollectionItem(SalableProductCollectionItem salableProductCollectionItem) {
		return namedQuery(readBySalableProductCollectionItem).parameter(SalableProductCollectionItemSaleCashRegisterMovement.FIELD_SALABLE_PRODUCT_COLLECTION_ITEM,
				salableProductCollectionItem).resultMany();
	}

	@Override
	public Collection<SalableProductCollectionItemSaleCashRegisterMovement> readBySaleCashRegisterMovement(SaleCashRegisterMovement saleCashRegisterMovement) {
		return namedQuery(readBySaleCashRegisterMovement).parameter(SalableProductCollectionItemSaleCashRegisterMovement.FIELD_SALE_CASH_REGISTER_MOVEMENT,
				saleCashRegisterMovement).resultMany();
	}
	
	
	

}
