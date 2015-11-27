package org.cyk.system.company.business.impl.structure;

import java.io.Serializable;

import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

public class CompanyDetails extends AbstractOutputDetails<Company> implements Serializable{
	private static final long serialVersionUID = -4741435164709063863L;
	
	@Input @InputText private String name,manager,signer;
	
	public CompanyDetails(Company company) {
		super(company);
		name = company.getName();
		if(company.getManager()!=null)
			manager = company.getManager().getNames();
		if(company.getSigner()!=null)
			signer = company.getSigner().getNames();
	}
}