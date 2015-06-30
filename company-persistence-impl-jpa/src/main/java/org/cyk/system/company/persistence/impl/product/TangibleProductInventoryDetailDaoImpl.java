package org.cyk.system.company.persistence.impl.product;

import java.util.Collection;

import org.cyk.system.company.model.product.TangibleProductInventory;
import org.cyk.system.company.model.product.TangibleProductInventoryDetail;
import org.cyk.system.company.persistence.api.product.TangibleProductInventoryDetailDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class TangibleProductInventoryDetailDaoImpl extends AbstractTypedDao<TangibleProductInventoryDetail> implements TangibleProductInventoryDetailDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readByTangibleProductInventory;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByTangibleProductInventory, "SELECT r FROM TangibleProductInventoryDetail r WHERE EXISTS "
				+ "( SELECT m FROM TangibleProductInventory m WHERE m.identifier = :tangibleProductInventoryId AND r MEMBER OF m.details )");
	}
	
	@Override
	public Collection<TangibleProductInventoryDetail> readByTangibleProductInventory(TangibleProductInventory tangibleProductInventory) {
		return namedQuery(readByTangibleProductInventory).parameter("tangibleProductInventoryId", tangibleProductInventory.getIdentifier()).resultMany();
	}

	
	
}
