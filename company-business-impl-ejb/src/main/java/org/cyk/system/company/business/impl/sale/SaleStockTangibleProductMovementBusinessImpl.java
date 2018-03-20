package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SaleStockTangibleProductMovementBusiness;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovement;
import org.cyk.system.company.persistence.api.sale.SaleStockTangibleProductMovementDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
@Deprecated
public class SaleStockTangibleProductMovementBusinessImpl extends AbstractTypedBusinessService<SaleStockTangibleProductMovement, SaleStockTangibleProductMovementDao> implements SaleStockTangibleProductMovementBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;
	
	@Inject
	public SaleStockTangibleProductMovementBusinessImpl(SaleStockTangibleProductMovementDao dao) {
		super(dao);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SaleStockTangibleProductMovement> findBySale(Sale sale) {
		return dao.readBySale(sale);
	}

}
