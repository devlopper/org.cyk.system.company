package org.cyk.system.company.ui.web.primefaces.structure;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.model.structure.EmploymentAgreement;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class EmploymentAgreementConsultPage extends AbstractConsultPage<EmploymentAgreement> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	

}
