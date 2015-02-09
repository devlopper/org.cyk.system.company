package org.cyk.system.company.model;

import java.io.Serializable;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.party.Party;

@Getter @Setter @Entity
public class Company extends Party implements Serializable {
 
	private static final long serialVersionUID = 2742833783679362737L;

	
	
}
