package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.sale.AbstractSale;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public abstract class AbstractSaleDetails<IDENTIFIABLE extends AbstractSale> extends AbstractSalableProductCollectionDetails<IDENTIFIABLE> implements Serializable {
	private static final long serialVersionUID = -1498269103849317057L;
	
	@Input @InputText
	protected String customer;
	
	public AbstractSaleDetails(IDENTIFIABLE identifiable) {
		super(identifiable);
		customer = formatUsingBusiness(identifiable.getCustomer());
	}
	
	@Override
	protected SalableProductCollection getSalableProductCollection() {
		return master.getSalableProductCollection();
	}
	
	/**/
	
	public static final String FIELD_CUSTOMER = "customer";
	
}