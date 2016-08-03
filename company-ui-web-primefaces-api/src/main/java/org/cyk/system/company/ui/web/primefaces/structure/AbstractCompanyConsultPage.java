package org.cyk.system.company.ui.web.primefaces.structure;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.structure.Company;
import org.cyk.system.root.model.party.Party;
import org.cyk.ui.web.primefaces.page.party.AbstractPartyConsultPage;

@Getter @Setter
public abstract class AbstractCompanyConsultPage extends AbstractPartyConsultPage<Company> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;

	@Override
	protected Party getParty() {
		return identifiable;
	}
					
}
