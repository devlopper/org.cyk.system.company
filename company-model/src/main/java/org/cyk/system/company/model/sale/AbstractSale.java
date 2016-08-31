package org.cyk.system.company.model.sale;

import java.io.Serializable;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.root.model.AbstractIdentifiable;

/**
 * The exchange of a commodity or money as the price of a good or a service.<br/>
 * A transaction between two parties where the buyer receives goods (tangible or intangible)
 * @author Christian Yao Komenan
 *
 */
@Getter @Setter @NoArgsConstructor @MappedSuperclass
public abstract class AbstractSale extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne protected Customer customer;
	@ManyToOne protected SalableProductCollection salableProductCollection;
	
	/**/
	
	public AccountingPeriod getAccountingPeriod() {
		return salableProductCollection==null ? null : salableProductCollection.getAccountingPeriod();
	}

	public Boolean getAutoComputeValueAddedTax() {
		return salableProductCollection==null ? null :salableProductCollection.getAutoComputeValueAddedTax();
	}
	
	/**/
	
	public static final String FIELD_CUSTOMER = "customer";
	public static final String FIELD_SALABLE_PRODUCT_COLLECTION = "salableProductCollection";
	
}
