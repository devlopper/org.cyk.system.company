package org.cyk.system.company.model.production;

import java.io.Serializable;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractEnumeration;

@Getter @Setter @NoArgsConstructor @Entity
public class ProductionEnergyType extends AbstractEnumeration implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	public static final String GAS = "GAS";
	public static final String FUEL = "FUEL";
	public static final String WOOD = "WOOD";
	
	public ProductionEnergyType(String code, String libelle) {
		super(code, libelle,null, null);
	}
	
	
}
