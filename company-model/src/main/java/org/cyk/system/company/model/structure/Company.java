package org.cyk.system.company.model.structure;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.party.Party;
import org.cyk.system.root.model.party.person.Person;

@Getter @Setter @NoArgsConstructor @Entity
public class Company extends Party implements Serializable {
 
	private static final long serialVersionUID = 2742833783679362737L;

	@ManyToOne private Person manager;
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public String getUiString() {
		return toString();
	}
}
