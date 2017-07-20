package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import org.cyk.system.company.business.impl.BalanceDetails;
import org.cyk.system.company.model.sale.SalableProductCollectionItemSaleCashRegisterMovement;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SalableProductCollectionItemSaleCashRegisterMovementDetails extends AbstractOutputDetails<SalableProductCollectionItemSaleCashRegisterMovement> implements Serializable {
	private static final long serialVersionUID = -6341285110719947720L;
	
	@Input @InputText private String salableProductCollectionItem;
	@Input @InputText private String saleCashRegisterMovement;
	@Input @InputText private String amount;
	@IncludeInputs private BalanceDetails balance = new BalanceDetails(null);
	
	public SalableProductCollectionItemSaleCashRegisterMovementDetails(SalableProductCollectionItemSaleCashRegisterMovement salableProductCollectionItemSaleCashRegisterMovement) {
		super(salableProductCollectionItemSaleCashRegisterMovement);
		salableProductCollectionItem = formatUsingBusiness(salableProductCollectionItemSaleCashRegisterMovement.getSalableProductCollectionItem());
		saleCashRegisterMovement = formatUsingBusiness(salableProductCollectionItemSaleCashRegisterMovement.getSaleCashRegisterMovement());
		balance.set(salableProductCollectionItemSaleCashRegisterMovement.getBalance());
		amount = formatNumber(salableProductCollectionItemSaleCashRegisterMovement.getAmount());
	}
	
	public static final String FIELD_SALABLE_PRODUCT_COLLECTION_ITEM = "salableProductCollectionItem";
	public static final String FIELD_SALE_CASH_REGISTER_MOVEMENT = "saleCashRegisterMovement";
	public static final String FIELD_AMOUNT = "amount";
	public static final String FIELD_BALANCE = "balance";
	
	/*public static String[] getFieldsToHide(){
		return new String[]{MovementDetails.FIELD_CODE,MovementDetails.FIELD_NAME};
	}*/

}