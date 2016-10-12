package org.cyk.system.company.ui.web.primefaces.adapter.enterpriseresourceplanning;

import java.io.Serializable;

import org.cyk.system.company.business.impl.structure.EmployeeBusinessImpl;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.structure.EmploymentAgreement;

public class EmployeeBusinessAdapter extends EmployeeBusinessImpl.Listener.Adapter.Default implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public EmployeeBusinessAdapter() {
		addCascadeToClass(EmploymentAgreement.class).addCascadeToReportTemplateCodes(CompanyConstant.REPORT_EMPLOYEE_EMPLOYMENT_CONTRACT,
				CompanyConstant.REPORT_EMPLOYEE_EMPLOYMENT_CERTIFICATE,CompanyConstant.REPORT_EMPLOYEE_WORK_CERTIFICATE);
	}
	
}
