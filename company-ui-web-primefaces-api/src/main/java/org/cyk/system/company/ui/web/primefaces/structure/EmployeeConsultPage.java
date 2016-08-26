package org.cyk.system.company.ui.web.primefaces.structure;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.business.impl.structure.EmploymentAgreementDetails;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.model.structure.EmploymentAgreement;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.party.AbstractActorConsultPage;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class EmployeeConsultPage extends AbstractActorConsultPage<Employee> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;

	private FormOneData<EmploymentAgreementDetails> employmentAgreementDetails;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void consultInitialisation() {
		super.consultInitialisation();
		@SuppressWarnings("rawtypes")
		DetailsConfigurationListener.Form.Adapter adapter = getDetailsConfiguration(EmploymentAgreementDetails.class).getFormConfigurationAdapter(EmploymentAgreement.class, EmploymentAgreementDetails.class);
		employmentAgreementDetails = createDetailsForm(EmploymentAgreementDetails.class, identifiable.getEmploymentAgreement(), adapter);
		employmentAgreementDetails.addControlSetListener(getDetailsConfiguration(EmploymentAgreementDetails.class).getFormControlSetAdapter(EmploymentAgreement.class));
	}
	
}
