package org.cyk.system.company.model.product;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.root.model.AbstractIdentifiable;

@Getter @Setter @NoArgsConstructor @Entity
public class SaledProductEmployee extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne
	private SaledProduct saledProduct;
	
	@ManyToOne
	private Employee employee;

	public SaledProductEmployee(SaledProduct saledProduct,Employee employee) {
		super();
		this.saledProduct = saledProduct;
		this.employee = employee;
	}
	
	
	
}
