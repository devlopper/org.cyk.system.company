package org.cyk.system.company.model.payment;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.root.model.AbstractEnumeration;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity @ModelBean(genderType=GenderType.FEMALE,crudStrategy=CrudStrategy.BUSINESS)
public class CashRegister extends AbstractEnumeration implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;
	
	@ManyToOne @NotNull private OwnedCompany ownedCompany;
	
	@OneToOne @NotNull private MovementCollection movementCollection;

	public CashRegister(String code, OwnedCompany ownedCompany,MovementCollection movementCollection) {
		super(code, code, null, null);
		this.ownedCompany = ownedCompany;
		this.movementCollection = movementCollection;
	}
	
}
