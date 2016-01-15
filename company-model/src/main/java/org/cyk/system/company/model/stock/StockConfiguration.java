package org.cyk.system.company.model.stock;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.cyk.system.root.model.AbstractModelElement;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Embeddable
public class StockConfiguration extends AbstractModelElement implements Serializable {

	private static final long serialVersionUID = 2700928054823690772L;

	private Boolean zeroQuantityAllowed = Boolean.FALSE;
	
	@Override
	public String getUiString() {
		return toString();
	}

}
