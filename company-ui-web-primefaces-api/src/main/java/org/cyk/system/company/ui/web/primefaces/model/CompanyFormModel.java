package org.cyk.system.company.ui.web.primefaces.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.structure.Company;
import org.cyk.ui.api.model.party.AbstractPartyFormModel;

@Getter @Setter
public class CompanyFormModel extends AbstractPartyFormModel<Company> implements Serializable {

	private static final long serialVersionUID = -3328823824725030136L;

	
}
