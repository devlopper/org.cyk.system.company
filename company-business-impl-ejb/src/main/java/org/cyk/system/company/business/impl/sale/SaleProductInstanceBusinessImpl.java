package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SaleProductInstanceBusiness;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.model.sale.SaleProductInstance;
import org.cyk.system.company.persistence.api.sale.SaleProductInstanceDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

public class SaleProductInstanceBusinessImpl extends AbstractTypedBusinessService<SaleProductInstance, SaleProductInstanceDao> implements SaleProductInstanceBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public SaleProductInstanceBusinessImpl(SaleProductInstanceDao dao) {
		super(dao);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SaleProductInstance> findBySaleProduct(SaleProduct saleProduct) {
		return dao.readBySaleProduct(saleProduct);
	}
	
}
