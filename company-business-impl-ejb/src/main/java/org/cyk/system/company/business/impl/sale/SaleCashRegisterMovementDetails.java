package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.payment.CashRegisterMovementDetails;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.system.root.business.impl.mathematics.MovementDetails;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs.Layout;

@Getter @Setter
public class SaleCashRegisterMovementDetails extends AbstractOutputDetails<SaleCashRegisterMovement> implements Serializable {
	private static final long serialVersionUID = -6341285110719947720L;
	
	@IncludeInputs(layout=Layout.VERTICAL) private CashRegisterMovementDetails cashRegisterMovementDetails;
	//@Input @InputText
	//@Sequence(direction=Direction.AFTER,field=MovementDetails.FIELD_ACTION)
	//private String balanceCumul;
	
	public SaleCashRegisterMovementDetails(SaleCashRegisterMovement saleCashRegisterMovement) {
		super(saleCashRegisterMovement);
		cashRegisterMovementDetails = new CashRegisterMovementDetails(saleCashRegisterMovement.getCashRegisterMovement());
		//balanceCumul = formatNumber(saleCashRegisterMovement.getBalance().getCumul());
	}
	
	public static String[] getFieldsToHide(){
		return new String[]{MovementDetails.FIELD_CODE,MovementDetails.FIELD_NAME};
	}
}