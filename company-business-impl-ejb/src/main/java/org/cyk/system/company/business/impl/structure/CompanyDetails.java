package org.cyk.system.company.business.impl.structure;

import java.io.Serializable;

import org.cyk.system.company.model.structure.Company;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CompanyDetails extends AbstractCompanyDetails<Company> implements Serializable {

	private static final long serialVersionUID = -1498269103849317057L;
	
	public CompanyDetails(Company company) {
		super(company);
	}
	
	@Override
	protected Company getCompany() {
		return getMaster();
	}
	
}