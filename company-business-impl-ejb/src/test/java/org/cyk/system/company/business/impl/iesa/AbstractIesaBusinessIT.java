package org.cyk.system.company.business.impl.iesa;

import org.cyk.system.company.business.impl.integration.AbstractBusinessIT;
import org.cyk.system.company.business.impl.structure.EmployeeBusinessImpl;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.structure.EmploymentAgreement;

public abstract class AbstractIesaBusinessIT extends AbstractBusinessIT {

	private static final long serialVersionUID = -5752455124275831171L;
	
	public AbstractIesaBusinessIT() {
		EmployeeBusinessImpl.Listener listener = new EmployeeBusinessImpl.Listener.Adapter.Default();
		listener.addCascadeToClass(EmploymentAgreement.class)
			.addCascadeToReportTemplateCodes(CompanyConstant.Code.ReportTemplate.EMPLOYEE_EMPLOYMENT_CONTRACT,
					CompanyConstant.Code.ReportTemplate.EMPLOYEE_EMPLOYMENT_CERTIFICATE,CompanyConstant.Code.ReportTemplate.EMPLOYEE_WORK_CERTIFICATE);
		EmployeeBusinessImpl.Listener.COLLECTION.add(listener);
	}
}
