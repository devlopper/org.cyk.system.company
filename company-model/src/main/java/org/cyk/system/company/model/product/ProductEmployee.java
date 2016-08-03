package org.cyk.system.company.model.product;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.root.model.AbstractIdentifiable;

@Getter @Setter @NoArgsConstructor @Entity @EqualsAndHashCode(callSuper=false,of={"product","employee"})
@Deprecated
//FIXME to be deleted or re think
public class ProductEmployee extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull
	private Product product;
	
	@ManyToOne @NotNull
	private Employee employee;
	
	private String comments;

	public ProductEmployee(Product product,Employee employee) {
		super();
		this.product = product;
		this.employee = employee;
	}
	
	
	
}
