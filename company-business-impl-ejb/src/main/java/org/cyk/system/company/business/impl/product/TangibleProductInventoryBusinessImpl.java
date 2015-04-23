package org.cyk.system.company.business.impl.product;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.product.TangibleProductInventoryBusiness;
import org.cyk.system.company.model.product.TangibleProductInventory;
import org.cyk.system.company.persistence.api.product.TangibleProductInventoryDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class TangibleProductInventoryBusinessImpl extends AbstractTypedBusinessService<TangibleProductInventory, TangibleProductInventoryDao> implements TangibleProductInventoryBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject
	public TangibleProductInventoryBusinessImpl(TangibleProductInventoryDao dao) {
		super(dao);
	}
	
	@Override
	public TangibleProductInventory create(TangibleProductInventory tangibleProductInventory) {
		tangibleProductInventory.setDate(universalTimeCoordinated());
		super.create(tangibleProductInventory);
		return tangibleProductInventory;
	}
	
}
