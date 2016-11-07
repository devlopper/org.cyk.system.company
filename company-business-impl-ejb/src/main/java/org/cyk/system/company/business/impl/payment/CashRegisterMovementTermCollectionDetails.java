package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.payment.CashRegisterMovementTermCollection;
import org.cyk.system.root.business.impl.AbstractCollectionDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public class CashRegisterMovementTermCollectionDetails extends AbstractCollectionDetails.Extends<CashRegisterMovementTermCollection> implements Serializable{
	
	private static final long serialVersionUID = -4741435164709063863L;
	
	@Input @InputText private String amount;
	
	public CashRegisterMovementTermCollectionDetails(CashRegisterMovementTermCollection cashRegisterMovementTermCollection) {
		super(cashRegisterMovementTermCollection);
		amount = formatNumber(cashRegisterMovementTermCollection.getAmount());
	}
	
	/**/
	
	public static final String FIELD_AMOUNT = "amount";
}