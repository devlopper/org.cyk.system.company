package org.cyk.system.company.ui.web.primefaces.structure;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.model.structure.EmploymentAgreement;
import org.cyk.system.company.model.structure.EmploymentAgreementType;
import org.cyk.ui.api.model.party.AbstractActorEditFormModel;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class EmployeeEditPage extends AbstractCrudOnePage<Employee> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	public static class Form extends AbstractActorEditFormModel.AbstractDefault.Default<Employee> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private EmploymentAgreementType employmentAgreementType;
		
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
