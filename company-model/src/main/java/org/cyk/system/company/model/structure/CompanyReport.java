package org.cyk.system.company.model.structure;

import java.io.Serializable;

import org.cyk.system.root.model.party.PartyReport;

public class CompanyReport extends PartyReport implements Serializable {

	private static final long serialVersionUID = 7967195187341455422L;

	@Override
	public void generate() {
		super.generate();
		image = inputStream(provider.companyLogo());
		name = provider.companyName();
	}
	
}
