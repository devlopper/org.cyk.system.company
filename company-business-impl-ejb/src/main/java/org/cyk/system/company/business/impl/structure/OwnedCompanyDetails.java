package org.cyk.system.company.business.impl.structure;

import java.io.Serializable;

import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.model.structure.OwnedCompany;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OwnedCompanyDetails extends AbstractCompanyDetails<OwnedCompany> implements Serializable {
	private static final long serialVersionUID = -1498269103849317057L;
	
	public OwnedCompanyDetails(OwnedCompany ownedCompany) {
		super(ownedCompany);
	}
	
	@Override
	protected Company getCompany() {
		return getMaster().getCompany();
	}
	
}