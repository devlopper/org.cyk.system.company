package org.cyk.system.company.ui.web.primefaces.structure;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;

@Getter @Setter
public abstract class AbstractOwnedCompanyConsultPage extends AbstractConsultPage<OwnedCompany> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
}
