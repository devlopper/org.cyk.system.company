package org.cyk.system.company.model.accounting;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.AbstractModelElement;
import org.cyk.system.root.model.generator.StringGenerator;

@Getter @Setter @Embeddable
public class SaleConfiguration extends AbstractModelElement implements Serializable {

	private static final long serialVersionUID = 2700928054823690772L;

	@OneToOne private StringGenerator saleIdentifierGenerator;

	@OneToOne private StringGenerator cashRegisterMovementIdentifierGenerator;

	@Override
	public String getUiString() {
		return toString();
	}

}
