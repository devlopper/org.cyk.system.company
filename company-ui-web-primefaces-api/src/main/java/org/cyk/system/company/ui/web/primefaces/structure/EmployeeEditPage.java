package org.cyk.system.company.ui.web.primefaces.structure;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.structure.EmploymentAgreementBusiness;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.model.structure.EmploymentAgreement;
import org.cyk.system.company.model.structure.EmploymentAgreementType;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.ui.api.model.party.AbstractActorEditFormModel;
import org.cyk.ui.web.primefaces.page.party.AbstractActorEditPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneRadio;

@Named @ViewScoped @Getter @Setter
public class EmployeeEditPage extends AbstractActorEditPage.AbstractDefault.Default<Employee> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;

	@SuppressWarnings("unchecked")
	@Override
	protected <T extends AbstractIdentifiable> T identifiableFromRequestParameter(Class<T> aClass, String identifierId) {
		Employee identifiable = (Employee) super.identifiableFromRequestParameter(aClass, identifierId);
		Collection<EmploymentAgreement> employmentAgreements = inject(EmploymentAgreementBusiness.class).findByEmployee(identifiable);
		if(identifiable!=null)
			identifiable.setEmploymentAgreement(employmentAgreements.isEmpty() ? null : employmentAgreements.iterator().next());
		return (T) identifiable;
	}
	
	public static class Form extends AbstractActorEditFormModel.AbstractDefault.Default<Employee> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneRadio @NotNull private EmploymentAgreementType employmentAgreementType;
		
		public static final String FIELD_EMPLOYMENT_AGREEMENT_TYPE = "employmentAgreementType";
		
		
		@Override
		public void read() {
			super.read();
			if(identifiable.getEmploymentAgreement()!=null)
				employmentAgreementType = identifiable.getEmploymentAgreement().getType();
		}
		
		@Override
		public void write() {
			super.write();
			if(identifiable.getEmploymentAgreement()==null && employmentAgreementType!=null)
				identifiable.setEmploymentAgreement(new EmploymentAgreement());
			
			if(identifiable.getEmploymentAgreement()!=null)
				identifiable.getEmploymentAgreement().setType(employmentAgreementType);
		}
	}

}
