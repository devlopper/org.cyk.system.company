package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleDetails extends AbstractOutputDetails<Sale> implements Serializable {
	private static final long serialVersionUID = -1498269103849317057L;
	
	@Input @InputText
	private String identifier,cost,balance,customer,date;
	
	public SaleDetails(Sale sale) {
		super(sale);
		this.identifier = sale.getComputedIdentifier();
		//this.cost = numberBusiness.format(sale.getCost());
		this.balance = numberBusiness.format(sale.getBalance().getValue().abs());
		this.customer = sale.getCustomer()==null?"":sale.getCustomer().getPerson().getNames();
		this.date = timeBusiness.formatDateTime(sale.getDate());
	}
}