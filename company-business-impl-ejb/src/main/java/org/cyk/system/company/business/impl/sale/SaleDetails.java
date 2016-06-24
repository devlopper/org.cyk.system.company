package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleDetails extends AbstractOutputDetails<Sale> implements Serializable {
	private static final long serialVersionUID = -1498269103849317057L;
	
	@Input @InputText
	private String identifier,externalIdentifier,cost,balance,customer,date;
	
	public SaleDetails(Sale sale) {
		super(sale);
		this.identifier = sale.getComputedIdentifier();
		this.externalIdentifier = sale.getExternalIdentifier();
		this.cost = formatNumber(sale.getCost().getValue());
		this.balance = formatNumber(sale.getBalance().getValue().abs());
		this.customer = sale.getCustomer()==null?Constant.EMPTY_STRING:(sale.getCustomer().getRegistration().getCode()+Constant.CHARACTER_SLASH+sale.getCustomer().getPerson().getNames());
		this.date = formatDateTime(sale.getDate());
	}
	
	/**/
	
	public static final String FIELD_IDENTIFIER = "identifier";
	public static final String FIELD_EXTERNAL_IDENTIFIER = "externalIdentifier";
	public static final String FIELD_COST = "cost";
	public static final String FIELD_BALANCE = "balance";
	public static final String FIELD_CUSTOMER = "customer";
	public static final String FIELD_DATE = "date";
}