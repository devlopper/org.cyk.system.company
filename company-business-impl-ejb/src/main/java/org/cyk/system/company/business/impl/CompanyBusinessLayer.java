package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cyk.system.company.business.api.service.CustomerBusiness;
import org.cyk.system.company.business.api.service.ServiceBusiness;
import org.cyk.system.company.business.api.service.ServiceCollectionBusiness;
import org.cyk.system.company.business.api.service.ServiceTypeBusiness;
import org.cyk.system.company.business.api.structure.DivisionBusiness;
import org.cyk.system.company.business.api.structure.DivisionTypeBusiness;
import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.model.service.Customer;
import org.cyk.system.company.model.service.Service;
import org.cyk.system.company.model.service.ServiceCollection;
import org.cyk.system.company.model.service.ServiceType;
import org.cyk.system.company.model.structure.Division;
import org.cyk.system.company.model.structure.DivisionType;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.business.impl.AbstractBusinessLayer;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER)
public class CompanyBusinessLayer extends AbstractBusinessLayer implements Serializable {

	private static final long serialVersionUID = -462780912429013933L;

	@Inject private CustomerBusiness customerBusiness;
	@Inject private EmployeeBusiness employeeBusiness;
	@Inject private ServiceTypeBusiness serviceTypeBusiness;
	@Inject private ServiceBusiness serviceBusiness;
	@Inject private DivisionTypeBusiness divisionTypeBusiness;
	@Inject private DivisionBusiness divisionBusiness;
	@Inject private ServiceCollectionBusiness serviceCollectionBusiness;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		registerResourceBundle("org.cyk.system.company.model.resources.entity", getClass().getClassLoader());
	}
	
	@Override
	public void createInitialData() {
		structure();
	}
	
	private void structure(){
		DivisionType department = new DivisionType(null, "DEPARTMENT", "Department");
        create(department);
        
		ServiceType prestation = new ServiceType(null, "PRESTATION", "Prestation");
        create(prestation);

    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void registerTypedBusinessBean(Map<Class<AbstractIdentifiable>, TypedBusiness<AbstractIdentifiable>> beansMap) {
        beansMap.put((Class)Employee.class, (TypedBusiness)employeeBusiness);
        beansMap.put((Class)Customer.class, (TypedBusiness)customerBusiness);
        beansMap.put((Class)ServiceType.class, (TypedBusiness)serviceTypeBusiness);
        beansMap.put((Class)Service.class, (TypedBusiness)serviceBusiness);
        beansMap.put((Class)DivisionType.class, (TypedBusiness)divisionTypeBusiness);
        beansMap.put((Class)Division.class, (TypedBusiness)divisionBusiness);
        beansMap.put((Class)ServiceCollection.class, (TypedBusiness)serviceCollectionBusiness);
    }

}
