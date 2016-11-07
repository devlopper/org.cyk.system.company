package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.payment.AbstractCashRegisterMovementDetails;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.root.business.impl.mathematics.MovementDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public class SaleCashRegisterMovementDetails extends AbstractCashRegisterMovementDetails<SaleCashRegisterMovement,Sale> implements Serializable {
	private static final long serialVersionUID = -6341285110719947720L;
	
	@Input @InputText
	private String sale;
	
	public SaleCashRegisterMovementDetails(SaleCashRegisterMovement saleCashRegisterMovement) {
		super(saleCashRegisterMovement);
		sale = formatUsingBusiness(saleCashRegisterMovement.getSale());
	}
	
	@Override
	protected Sale getCollection() {
		return master.getSale();
	}
	
	@Override
	protected CashRegisterMovement getCashRegisterMovement() {
		return master.getCashRegisterMovement();
	}

	public static final String FIELD_SALE = "sale";
	public static final String FIELD_CASH_REGISTER_MOVEMENT = "cashRegisterMovement";
	
	public static String[] getFieldsToHide(){
		return new String[]{MovementDetails.FIELD_CODE,MovementDetails.FIELD_NAME};
	}

}