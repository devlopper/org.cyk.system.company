package org.cyk.system.company.model.service;

import java.io.Serializable;

import javax.persistence.Entity;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import org.cyk.system.root.model.party.person.AbstractActor;

@Getter @Setter @NoArgsConstructor @Entity
public class Customer extends AbstractActor implements Serializable {

	private static final long serialVersionUID = 1371797411549893368L;

}
