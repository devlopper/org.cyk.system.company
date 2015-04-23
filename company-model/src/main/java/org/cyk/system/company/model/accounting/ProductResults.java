package org.cyk.system.company.model.accounting;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractModelElement;
import org.cyk.system.root.model.mathematics.Sort;

@Getter @Setter @Embeddable @NoArgsConstructor @AllArgsConstructor
public class ProductResults extends AbstractModelElement implements Serializable {

	private static final long serialVersionUID = -344733168050282039L;
	
	@Embedded private Sort numberOfSalesSort = new Sort();
	
	@Override
	public String getUiString() {
		return null;
	}
	
}
