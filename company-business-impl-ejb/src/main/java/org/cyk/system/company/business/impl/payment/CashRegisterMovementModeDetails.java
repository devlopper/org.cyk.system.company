package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.payment.CashRegisterMovementMode;
import org.cyk.system.root.business.impl.AbstractEnumerationDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public class CashRegisterMovementModeDetails extends AbstractEnumerationDetails<CashRegisterMovementMode> implements Serializable{
	private static final long serialVersionUID = -4741435164709063863L;
	
	@Input @InputText private String supportDocumentIdentifier;
	
	public CashRegisterMovementModeDetails(CashRegisterMovementMode cashRegisterMovementMode) {
		super(cashRegisterMovementMode);
		supportDocumentIdentifier = formatResponse(cashRegisterMovementMode.getSupportDocumentIdentifier());
	}
	
	/**/
	
	public static final String FIELD_SUPPORT_DOCUMENT_IDENTIFIER = "supportDocumentIdentifier";
}