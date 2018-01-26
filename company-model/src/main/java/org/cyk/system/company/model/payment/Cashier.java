package org.cyk.system.company.model.payment;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
@ModelBean(crudStrategy=CrudStrategy.ENUMERATION,genderType=GenderType.MALE)
@Deprecated
public class Cashier extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull private Person person;
	
	@ManyToOne @NotNull private CashRegister cashRegister;
	
	@Override
	public String getUiString() {
		return person.getUiString()+Constant.CHARACTER_SLASH+cashRegister.getUiString();
	}
	
	public static final String FIELD_PERSON = "person";
	public static final String FIELD_CASH_REGISTER = "cashRegister";
	
}
