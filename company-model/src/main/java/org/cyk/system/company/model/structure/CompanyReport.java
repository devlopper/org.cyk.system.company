package org.cyk.system.company.model.structure;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.party.PartyReport;

@Getter @Setter
public class CompanyReport extends PartyReport implements Serializable {

	private static final long serialVersionUID = 7967195187341455422L;

	private Boolean valueAddedTaxCollected = Boolean.FALSE;
	
	@Override
	public void generate() {
		super.generate();
		if(Boolean.TRUE.equals(globalIdentifier.getGenerateImage()))
			globalIdentifier.setImage(inputStream(provider.companyLogo().getBytes()));
		globalIdentifier.setName(provider.companyName());
	}
	
}
