package org.cyk.system.company.persistence.api.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleIdentifiableGlobalIdentifier;
import org.cyk.system.root.persistence.api.globalidentification.JoinGlobalIdentifierDao;

public interface SaleIdentifiableGlobalIdentifierDao extends JoinGlobalIdentifierDao<SaleIdentifiableGlobalIdentifier,SaleIdentifiableGlobalIdentifier.SearchCriteria> {

	Collection<SaleIdentifiableGlobalIdentifier> readBySales(Collection<Sale> sales);
	Collection<SaleIdentifiableGlobalIdentifier> readBySale(Sale sale);
	
}
