package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.BalanceDetails;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public class SaleDetails extends AbstractSaleDetails<Sale> implements Serializable {
	private static final long serialVersionUID = -1498269103849317057L;
	
	@Input @InputText private String cashRegisterMovementTermCollection;
	@IncludeInputs private BalanceDetails balance = new BalanceDetails(null);
	
	public SaleDetails(Sale sale) {
		super(sale);
		balance.set(sale.getBalance());
		//cashRegisterMovementTermCollection = formatUsingBusiness(sale.getCashRegisterMovementTermCollection());
	}
	
	/**/
	
	public static final String FIELD_BALANCE = "balance";
	
}