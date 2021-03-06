package org.cyk.system.company.business.impl.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.business.api.structure.EmploymentAgreementBusiness;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.model.structure.EmploymentAgreement;
import org.cyk.system.company.persistence.api.structure.EmployeeDao;
import org.cyk.system.company.persistence.api.structure.EmploymentAgreementDao;
import org.cyk.system.root.business.impl.party.person.AbstractActorBusinessImpl;

public class EmployeeBusinessImpl extends AbstractActorBusinessImpl<Employee, EmployeeDao,Employee.SearchCriteria> implements EmployeeBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public EmployeeBusinessImpl(EmployeeDao dao) {
		super(dao);
	}
	
	@Override
	protected Collection<? extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener<?>> getListeners() {
		return Listener.COLLECTION;
	}
	
	/**/
	
	public static interface Listener extends org.cyk.system.root.business.impl.party.person.AbstractActorBusinessImpl.Listener<Employee>{
		
		Collection<Listener> COLLECTION = new ArrayList<>();
		
		/**/

		public static class Adapter extends org.cyk.system.root.business.impl.party.person.AbstractActorBusinessImpl.Listener.Adapter.Default<Employee> implements Listener, Serializable {
			private static final long serialVersionUID = -1625238619828187690L;
			
			/**/
			@Getter @Setter
			public static class Default extends Listener.Adapter implements Serializable {
				private static final long serialVersionUID = -1625238619828187690L;
				
				/**/
				
				@Override
				public void afterCreate(Employee employee) {
					super.afterCreate(employee);
					if(containsCascadeToClass(EmploymentAgreement.class) && employee.getEmploymentAgreement()!=null && employee.getEmploymentAgreement().getIdentifier()==null){
						createEmploymentAgreement(employee);
					}		
				}
				
				@Override
				public void afterUpdate(Employee employee) {
					super.afterUpdate(employee);
					if(containsCascadeToClass(EmploymentAgreement.class)){
						if(employee.getEmploymentAgreement()!=null && employee.getEmploymentAgreement().getIdentifier()==null)
							createEmploymentAgreement(employee);
						else if(employee.getEmploymentAgreement()!=null)
							inject(EmploymentAgreementBusiness.class).update(employee.getEmploymentAgreement());
					}
				}
				
				private void createEmploymentAgreement(Employee employee){
					employee.getEmploymentAgreement().setEmployee(employee);
					inject(EmploymentAgreementBusiness.class).create(employee.getEmploymentAgreement());
				}
				
				@Override
				public void beforeDelete(Employee employee) {
					super.beforeDelete(employee);
					if(containsCascadeToClass(EmploymentAgreement.class)){
						for(EmploymentAgreement employmentAgreement : inject(EmploymentAgreementDao.class).readByEmployee(employee))
							inject(EmploymentAgreementBusiness.class).delete(employmentAgreement);
					}
				}
				
				/**/
				
				public static class EnterpriseResourcePlanning extends EmployeeBusinessImpl.Listener.Adapter.Default implements Serializable {
					
					private static final long serialVersionUID = 1L;

					public EnterpriseResourcePlanning() {
						addCascadeToClass(EmploymentAgreement.class).addCascadeToReportTemplateCodes(CompanyConstant.Code.ReportTemplate.EMPLOYEE_EMPLOYMENT_CONTRACT,
								CompanyConstant.Code.ReportTemplate.EMPLOYEE_EMPLOYMENT_CERTIFICATE,CompanyConstant.Code.ReportTemplate.EMPLOYEE_WORK_CERTIFICATE);
					}
					
				}

			}
			
		}
		
	}
}
