package org.cyk.system.company.model.service;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;

@Getter @Setter @NoArgsConstructor @Entity
public class ServiceDelivery extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne
	private Service service;
	
	@ManyToOne
	private Invoice invoice;

	public ServiceDelivery(Service service, Invoice invoice) {
		super();
		this.service = service;
		this.invoice = invoice;
	}
	
	
}
