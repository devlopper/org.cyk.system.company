package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.system.root.business.impl.mathematics.MovementDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public class SaleCashRegisterMovementDetails extends AbstractOutputDetails<SaleCashRegisterMovement> implements Serializable {
	private static final long serialVersionUID = -6341285110719947720L;
	
	//@IncludeInputs(layout=Layout.VERTICAL) private CashRegisterMovementDetails cashRegisterMovementDetails;
	@Input @InputText
	//@Sequence(direction=Direction.AFTER,field=MovementDetails.FIELD_ACTION)
	private String sale,cashRegisterMovement;
	
	public SaleCashRegisterMovementDetails(SaleCashRegisterMovement saleCashRegisterMovement) {
		super(saleCashRegisterMovement);
		//cashRegisterMovementDetails = new CashRegisterMovementDetails(saleCashRegisterMovement.getCashRegisterMovement());
		//balanceCumul = formatNumber(saleCashRegisterMovement.getBalance().getCumul());
		sale = formatUsingBusiness(saleCashRegisterMovement.getSale());
		cashRegisterMovement = formatUsingBusiness(saleCashRegisterMovement.getCashRegisterMovement());
	}

	public static final String FIELD_SALE = "sale";
	public static final String FIELD_CASH_REGISTER_MOVEMENT = "cashRegisterMovement";
	
	public static String[] getFieldsToHide(){
		return new String[]{MovementDetails.FIELD_CODE,MovementDetails.FIELD_NAME};
	}
}