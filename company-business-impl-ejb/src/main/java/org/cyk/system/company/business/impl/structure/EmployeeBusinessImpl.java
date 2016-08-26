package org.cyk.system.company.business.impl.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.business.api.structure.EmploymentAgreementBusiness;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.model.structure.EmploymentAgreement;
import org.cyk.system.company.persistence.api.structure.EmployeeDao;
import org.cyk.system.company.persistence.api.structure.EmploymentAgreementDao;
import org.cyk.system.root.business.impl.party.person.AbstractActorBusinessImpl;
import org.cyk.utility.common.ListenerUtils;

import lombok.Getter;
import lombok.Setter;

@Stateless
public class EmployeeBusinessImpl extends AbstractActorBusinessImpl<Employee, EmployeeDao,Employee.SearchCriteria> implements EmployeeBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public EmployeeBusinessImpl(EmployeeDao dao) {
		super(dao);
	}
	
	@Override
	public Employee create(final Employee employee) {
		listenerUtils.execute(Listener.COLLECTION, new ListenerUtils.VoidMethod<Listener>(){
			@Override
			public void execute(Listener listener) {
				listener.beforeCreate(employee);
			}});
		super.create(employee);
		listenerUtils.execute(Listener.COLLECTION, new ListenerUtils.VoidMethod<Listener>(){
			@Override
			public void execute(Listener listener) {
				listener.afterCreate(employee);
			}});
		return employee;
	}
	
	@Override
	public Employee update(final Employee employee) {
		listenerUtils.execute(Listener.COLLECTION, new ListenerUtils.VoidMethod<Listener>(){
			@Override
			public void execute(Listener listener) {
				listener.beforeUpdate(employee);
			}});
		super.update(employee);
		listenerUtils.execute(Listener.COLLECTION, new ListenerUtils.VoidMethod<Listener>(){
			@Override
			public void execute(Listener listener) {
				listener.afterUpdate(employee);
			}});
		return employee;
	}
	
	@Override
	public Employee delete(final Employee employee) {
		listenerUtils.execute(Listener.COLLECTION, new ListenerUtils.VoidMethod<Listener>(){
			@Override
			public void execute(Listener listener) {
				listener.beforeDelete(employee);
			}});
		super.delete(employee);
		listenerUtils.execute(Listener.COLLECTION, new ListenerUtils.VoidMethod<Listener>(){
			@Override
			public void execute(Listener listener) {
				listener.afterDelete(employee);
			}});
		return employee;
	}
	
	/**/
	
	public static interface Listener extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener<Employee>{
		
		Collection<Listener> COLLECTION = new ArrayList<>();
		
		/**/

		public static class Adapter extends org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl.Listener.Adapter<Employee> implements Listener, Serializable {
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
				public void beforeUpdate(Employee employee) {
					super.beforeUpdate(employee);
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
						EmploymentAgreement employmentAgreement = inject(EmploymentAgreementDao.class).readByEmployee(employee);
						if(employmentAgreement!=null)
							inject(EmploymentAgreementBusiness.class).delete(employmentAgreement);
					}
				}
			}
			
		}
		
	}
}
