package org.cyk.system.company.business.impl.stock;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.stock.StockableProductStoresTransferAcknowledgementBusiness;
import org.cyk.system.company.model.stock.StockableProductStoresTransferAcknowledgement;
import org.cyk.system.company.persistence.api.stock.StockableProductStoresTransferAcknowledgementDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

public class StockableProductStoresTransferAcknowledgementBusinessImpl extends AbstractTypedBusinessService<StockableProductStoresTransferAcknowledgement, StockableProductStoresTransferAcknowledgementDao> implements StockableProductStoresTransferAcknowledgementBusiness,Serializable {
	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public StockableProductStoresTransferAcknowledgementBusinessImpl(StockableProductStoresTransferAcknowledgementDao dao) {
		super(dao);
	}
	
}
