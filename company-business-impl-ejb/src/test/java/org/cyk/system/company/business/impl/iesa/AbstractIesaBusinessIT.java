package org.cyk.system.company.business.impl.iesa;

import java.util.Locale;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.impl.AbstractCompanyReportProducer;
import org.cyk.system.company.business.impl.integration.enterpriseresourceplanning.AbstractEnterpriseResourcePlanningBusinessIT;
import org.cyk.system.company.business.impl.structure.EmployeeBusinessImpl;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.structure.EmploymentAgreement;
import org.cyk.system.root.business.impl.PersistDataListener;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.RootConstant;
import org.cyk.utility.common.generator.AbstractGeneratable;

public abstract class AbstractIesaBusinessIT extends AbstractEnterpriseResourcePlanningBusinessIT {

	private static final long serialVersionUID = -5752455124275831171L;
	
	@Override
	protected void populate() {
		AbstractGeneratable.Listener.Adapter.Default.LOCALE = Locale.ENGLISH;
    	PersistDataListener.COLLECTION.add(new PersistDataListener.Adapter.Default(){
			private static final long serialVersionUID = -950053441831528010L;
			@SuppressWarnings("unchecked")
			@Override
			public <T> T processPropertyValue(Class<?> aClass,String instanceCode, String name, T value) {
				if(ArrayUtils.contains(new String[]{CompanyConstant.Code.File.DOCUMENT_HEADER}, instanceCode)){
					if(PersistDataListener.RELATIVE_PATH.equals(name))
						return (T) "/report/iesa/salecashregistermovementlogo.png";
				}
				return super.processPropertyValue(aClass, instanceCode, name, value);
			}
		});
		super.populate();
	}
	
	public AbstractIesaBusinessIT() {
		EmployeeBusinessImpl.Listener listener = new EmployeeBusinessImpl.Listener.Adapter.Default();
		listener.addCascadeToClass(EmploymentAgreement.class)
			.addCascadeToReportTemplateCodes(CompanyConstant.Code.ReportTemplate.EMPLOYEE_EMPLOYMENT_CONTRACT,
					CompanyConstant.Code.ReportTemplate.EMPLOYEE_EMPLOYMENT_CERTIFICATE,CompanyConstant.Code.ReportTemplate.EMPLOYEE_WORK_CERTIFICATE);
		EmployeeBusinessImpl.Listener.COLLECTION.add(listener);
		
		AbstractCompanyReportProducer.Listener.COLLECTION.add(new AbstractCompanyReportProducer.Listener.Adapter.Default(){
			private static final long serialVersionUID = 215473098986115952L;
			
			@Override
			public String[] getCustomerPersonRelationshipTypeRoleCodes(AbstractIdentifiable identifiable) {
				return new String[]{RootConstant.Code.PersonRelationshipTypeRole.FAMILY_PARENT_FATHER,RootConstant.Code.PersonRelationshipTypeRole.FAMILY_PARENT_MOTHER};
			}
			
			@Override
			public String getCustomerLabel(AbstractIdentifiable identifiable) {
				return "Parent";
			}
		});
	}
	
}
