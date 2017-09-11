package org.cyk.system.company.model.structure;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

import com.sun.istack.NotNull;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class OwnedCompany extends AbstractIdentifiable implements Serializable {
 
	private static final long serialVersionUID = 2742833783679362737L;

	@ManyToOne @NotNull private Company company;
	
	@Override
	public String toString() {
		return company == null ? super.toString() : company.toString();
	}
	
	@Override
	public String getUiString() {
		return toString();
	}
	
	public static final String FIELD_COMPANY = "company";
	
}
