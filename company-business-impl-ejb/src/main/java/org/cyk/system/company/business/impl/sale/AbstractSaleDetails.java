package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.sale.AbstractSale;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public abstract class AbstractSaleDetails<IDENTIFIABLE extends AbstractSale> extends AbstractOutputDetails<IDENTIFIABLE> implements Serializable {
	private static final long serialVersionUID = -1498269103849317057L;
	
	@Input @InputText
	protected String customer,salableProductCollection;
	
	public AbstractSaleDetails(IDENTIFIABLE identifiable) {
		super(identifiable);
		customer = formatUsingBusiness(identifiable.getCustomer());
		salableProductCollection = formatUsingBusiness(identifiable.getSalableProductCollection());
		/*
		this.identifier = sale.getComputedIdentifier();
		this.externalIdentifier = sale.getExternalIdentifier();
		this.cost = formatNumber(sale.getCost().getValue());
		this.balance = formatNumber(sale.getBalance().getValue().abs());
		this.customer = sale.getCustomer()==null?Constant.EMPTY_STRING:(sale.getCustomer().getCode()+Constant.CHARACTER_SLASH+sale.getCustomer().getPerson().getNames());
		this.date = formatDateTime(sale.getDate());
		*/
	}
	
	/**/
	
	public static final String FIELD_CUSTOMER = "customer";
	public static final String FIELD_SALABLE_PRODUCT_COLLECTION = "salableProductCollection";
}