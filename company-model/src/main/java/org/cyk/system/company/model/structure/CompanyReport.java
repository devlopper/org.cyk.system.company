package org.cyk.system.company.model.structure;

import java.io.InputStream;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.party.PartyReport;

@Getter @Setter
public class CompanyReport extends PartyReport implements Serializable {

	private static final long serialVersionUID = 7967195187341455422L;

	private Boolean valueAddedTaxCollected = Boolean.FALSE;
	
	private InputStream backgroundImage;
	private Boolean generateBackground=Boolean.FALSE;
	private Boolean draftBackground=Boolean.FALSE;
	
	@Override
	public void generate() {
		super.generate();
		if(Boolean.TRUE.equals(globalIdentifier.getGenerateImage()))
			globalIdentifier.setImage(inputStream(provider.companyLogo().getBytes()));
		if(Boolean.TRUE.equals(generateBackground))
			if(Boolean.TRUE.equals(draftBackground))
				backgroundImage = inputStream(provider.documentDraftBackground().getBytes());
			else
				backgroundImage = inputStream(provider.companyLogo().getBytes());
		globalIdentifier.setName(provider.companyName());
	}
	
}
