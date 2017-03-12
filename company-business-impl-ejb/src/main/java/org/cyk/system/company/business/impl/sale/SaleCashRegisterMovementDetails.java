package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.root.business.impl.AbstractCollectionItemDetails;
import org.cyk.system.root.business.impl.mathematics.MovementDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleCashRegisterMovementDetails extends AbstractCollectionItemDetails<SaleCashRegisterMovement, SaleCashRegisterMovementCollection> implements Serializable {
	private static final long serialVersionUID = -6341285110719947720L;
	
	@Input @InputText
	private String amount,balance;
	
	public SaleCashRegisterMovementDetails(SaleCashRegisterMovement saleCashRegisterMovement) {
		super(saleCashRegisterMovement);
	}
	
	@Override
	public void setMaster(SaleCashRegisterMovement master) {
		super.setMaster(master);
		if(master==null){
			
		}else{
			amount = formatNumber(master.getAmount());
			balance = formatNumber(master.getBalance().getValue());
		}
	}
	
	@Override
	protected SaleCashRegisterMovementCollection getCollection() {
		return master.getCollection();
	}
	
	public static final String FIELD_AMOUNT = "amount";
	public static final String FIELD_BALANCE = "balance";
	
	public static String[] getFieldsToHide(){
		return new String[]{MovementDetails.FIELD_CODE,MovementDetails.FIELD_NAME};
	}

	

}