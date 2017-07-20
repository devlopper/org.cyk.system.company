package org.cyk.system.company.ui.web.primefaces.structure;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.model.structure.EmploymentAgreement;
import org.cyk.system.company.model.structure.EmploymentAgreementType;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputChoiceAutoComplete;
import org.cyk.utility.common.annotation.user.interfaces.InputOneAutoComplete;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class EmploymentAgreementEditPage extends AbstractCrudOnePage<EmploymentAgreement> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	@Getter @Setter
	public static class Form extends AbstractFormModel<EmploymentAgreement> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private EmploymentAgreementType type; 
		
		@Input @InputChoice @InputChoiceAutoComplete @InputOneChoice @InputOneAutoComplete @NotNull private Employee employee;
		
		
		/**/
		
		public static final String FIELD_TYPE = "type";
		public static final String FIELD_EMPLOYEE = "employee";
	}
	
}
