package org.cyk.system.company.model.structure;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.cyk.system.root.model.AbstractIdentifiable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity
public class OwnedCompany extends AbstractIdentifiable implements Serializable {
 
	private static final long serialVersionUID = 2742833783679362737L;

	@ManyToOne private Company company;
	
	@NotNull @Column(nullable=false)
	private Boolean selected;

	@Override
	public String toString() {
		return company.toString();
	}
	
	@Override
	public String getUiString() {
		return toString();
	}
	
}
