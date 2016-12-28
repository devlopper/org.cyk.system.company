package org.cyk.system.company.persistence.api.sale;

import org.cyk.system.company.model.sale.SaleIdentifiableGlobalIdentifier;
import org.cyk.system.root.persistence.api.globalidentification.JoinGlobalIdentifierDao;

public interface SaleIdentifiableGlobalIdentifierDao extends JoinGlobalIdentifierDao<SaleIdentifiableGlobalIdentifier,SaleIdentifiableGlobalIdentifier.SearchCriteria> {
	
}
