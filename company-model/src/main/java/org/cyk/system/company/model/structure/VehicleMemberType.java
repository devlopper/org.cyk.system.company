package org.cyk.system.company.model.structure;

import java.io.Serializable;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
@ModelBean(genderType=GenderType.MALE,crudStrategy=CrudStrategy.BUSINESS)
public class VehicleMemberType extends AbstractIdentifiable implements Serializable {

	private static final long serialVersionUID = 376048854435296766L;

	private Person driver;
	
	private String registrationIdentifier;
	
	
}
