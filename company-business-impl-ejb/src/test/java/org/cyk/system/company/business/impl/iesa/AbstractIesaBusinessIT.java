package org.cyk.system.company.business.impl.iesa;

import javax.inject.Inject;

import org.cyk.system.company.business.impl.AbstractCompanyReportProducer;
import org.cyk.system.company.business.impl.integration.enterpriseresourceplanning.AbstractEnterpriseResourcePlanningBusinessIT;
import org.cyk.system.company.business.impl.structure.EmployeeBusinessImpl;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.structure.EmploymentAgreement;
import org.cyk.system.root.business.impl.AbstractFakedDataProducer;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.RootConstant;

public abstract class AbstractIesaBusinessIT extends AbstractEnterpriseResourcePlanningBusinessIT {

	private static final long serialVersionUID = -5752455124275831171L;
	
	@Inject protected IesaFakedDataProducer dataProducer;
	
	public AbstractIesaBusinessIT() {
		EmployeeBusinessImpl.Listener listener = new EmployeeBusinessImpl.Listener.Adapter.Default();
		listener.addCascadeToClass(EmploymentAgreement.class)
			.addCascadeToReportTemplateCodes(CompanyConstant.Code.ReportTemplate.EMPLOYEE_EMPLOYMENT_CONTRACT,
					CompanyConstant.Code.ReportTemplate.EMPLOYEE_EMPLOYMENT_CERTIFICATE,CompanyConstant.Code.ReportTemplate.EMPLOYEE_WORK_CERTIFICATE);
		EmployeeBusinessImpl.Listener.COLLECTION.add(listener);
		
		AbstractCompanyReportProducer.Listener.COLLECTION.add(new AbstractCompanyReportProducer.Listener.Adapter.Default(){
			private static final long serialVersionUID = 215473098986115952L;
			
			@Override
			public String[] getCustomerPersonRelationshipTypeCodes(AbstractIdentifiable identifiable) {
				return new String[]{RootConstant.Code.PersonRelationshipType.FAMILY_FATHER,RootConstant.Code.PersonRelationshipType.FAMILY_MOTHER};
			}
			
			@Override
			public String getCustomerLabel(AbstractIdentifiable identifiable) {
				return "Parent";
			}
		});
	}
	
	@Override
    protected AbstractFakedDataProducer getFakedDataProducer() {
    	return dataProducer;
    }
}
