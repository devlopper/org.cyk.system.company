package org.cyk.system.company.model.structure;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class EmploymentAgreement extends AbstractIdentifiable implements Serializable {
 
	private static final long serialVersionUID = 2742833783679362737L;

	@ManyToOne @NotNull private Employee employee;
	
	@ManyToOne @NotNull private EmploymentAgreementType type;
	
}
