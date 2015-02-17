package org.cyk.system.company.model.service;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.root.model.AbstractIdentifiable;

@Getter @Setter @NoArgsConstructor @Entity
public class ServiceDeliveryEmployee extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne
	private ServiceDelivery serviceDelivery;
	
	@ManyToOne
	private Employee employee;

	public ServiceDeliveryEmployee(ServiceDelivery serviceDelivery,Employee employee) {
		super();
		this.serviceDelivery = serviceDelivery;
		this.employee = employee;
	}
	
	
	
}
