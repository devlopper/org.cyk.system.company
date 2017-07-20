package org.cyk.system.company.business.impl.structure;

import java.io.Serializable;

import org.cyk.system.company.model.structure.EmploymentAgreement;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmploymentAgreementDetails extends AbstractOutputDetails<EmploymentAgreement> implements Serializable {

	private static final long serialVersionUID = -1498269103849317057L;
	
	@Input @InputText private String type,employee;
	
	public EmploymentAgreementDetails(EmploymentAgreement employmentAgreement) {
		super(employmentAgreement);
		type = formatUsingBusiness(employmentAgreement.getType());
		employee = formatUsingBusiness(employmentAgreement.getEmployee());
	}
	
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_EMPLOYEE = "employee";
	
}