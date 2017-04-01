package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SaleIdentifiableGlobalIdentifierBusiness;
import org.cyk.system.company.model.sale.SaleIdentifiableGlobalIdentifier;
import org.cyk.system.company.persistence.api.sale.SaleIdentifiableGlobalIdentifierDao;
import org.cyk.system.root.business.impl.globalidentification.AbstractJoinGlobalIdentifierBusinessImpl;

public class SaleIdentifiableGlobalIdentifierBusinessImpl extends AbstractJoinGlobalIdentifierBusinessImpl<SaleIdentifiableGlobalIdentifier, SaleIdentifiableGlobalIdentifierDao,SaleIdentifiableGlobalIdentifier.SearchCriteria> implements SaleIdentifiableGlobalIdentifierBusiness,Serializable {

	private static final long serialVersionUID = -3799482462496328200L;

	@Inject
	public SaleIdentifiableGlobalIdentifierBusinessImpl(SaleIdentifiableGlobalIdentifierDao dao) {
		super(dao); 
	}
	
	@Override
	protected void beforeDelete(SaleIdentifiableGlobalIdentifier saleIdentifiableGlobalIdentifier) {
		super.beforeDelete(saleIdentifiableGlobalIdentifier);
		saleIdentifiableGlobalIdentifier.setSale(null);
		saleIdentifiableGlobalIdentifier.setIdentifiableGlobalIdentifier(null);
		//System.out.println("SaleIdentifiableGlobalIdentifierBusinessImpl.beforeDelete()");
	}
	
	@Override
	protected void deleteFileIdentifiableGlobalIdentifier(SaleIdentifiableGlobalIdentifier arg0) {}
	
}
