package org.cyk.system.company.business.impl.iesa;

import javax.inject.Inject;

import org.cyk.system.company.business.impl.integration.enterpriseresourceplanning.AbstractEnterpriseResourcePlanningBusinessIT;
import org.cyk.system.company.business.impl.structure.EmployeeBusinessImpl;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.structure.EmploymentAgreement;
import org.cyk.system.root.business.impl.AbstractFakedDataProducer;

public abstract class AbstractIesaBusinessIT extends AbstractEnterpriseResourcePlanningBusinessIT {

	private static final long serialVersionUID = -5752455124275831171L;
	
	@Inject protected IesaFakedDataProducer dataProducer;
	
	public AbstractIesaBusinessIT() {
		EmployeeBusinessImpl.Listener listener = new EmployeeBusinessImpl.Listener.Adapter.Default();
		listener.addCascadeToClass(EmploymentAgreement.class)
			.addCascadeToReportTemplateCodes(CompanyConstant.Code.ReportTemplate.EMPLOYEE_EMPLOYMENT_CONTRACT,
					CompanyConstant.Code.ReportTemplate.EMPLOYEE_EMPLOYMENT_CERTIFICATE,CompanyConstant.Code.ReportTemplate.EMPLOYEE_WORK_CERTIFICATE);
		EmployeeBusinessImpl.Listener.COLLECTION.add(listener);
	}
	
	@Override
    protected AbstractFakedDataProducer getFakedDataProducer() {
    	return dataProducer;
    }
}
