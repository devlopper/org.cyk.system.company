package org.cyk.system.company.business.api.structure;

import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.root.business.api.TypedBusiness;

public interface OwnedCompanyBusiness extends TypedBusiness<OwnedCompany> {

	OwnedCompany findDefaultOwnedCompany();
	
}
