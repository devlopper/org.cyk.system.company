package org.cyk.system.company.business.impl.product;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.TangibleProductInventoryBusiness;
import org.cyk.system.company.model.product.TangibleProductInventory;
import org.cyk.system.company.persistence.api.product.TangibleProductInventoryDao;
import org.cyk.system.company.persistence.api.product.TangibleProductInventoryDetailDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

public class TangibleProductInventoryBusinessImpl extends AbstractTypedBusinessService<TangibleProductInventory, TangibleProductInventoryDao> implements TangibleProductInventoryBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject private TangibleProductInventoryDetailDao tangibleProductInventoryDetailDao;
	
	@Inject
	public TangibleProductInventoryBusinessImpl(TangibleProductInventoryDao dao) {
		super(dao);
	}
	
	@Override
	public TangibleProductInventory create(TangibleProductInventory tangibleProductInventory) {
		if(tangibleProductInventory.getDate()==null)
			tangibleProductInventory.setDate(universalTimeCoordinated());
		super.create(tangibleProductInventory);
		return tangibleProductInventory;
	}
	
	@Override
	public void load(TangibleProductInventory tangibleProductInventory) {
		super.load(tangibleProductInventory);
		tangibleProductInventory.setDetails(tangibleProductInventoryDetailDao.readByTangibleProductInventory(tangibleProductInventory));
	}
	
}
