package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.search.DefaultSearchCriteria;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The exchange of a commodity or money as the price of a good or a service.<br/>
 * A transaction between two parties where the buyer receives goods (tangible or intangible)
 * @author Christian Yao Komenan
 *
 */
@Getter @Setter @NoArgsConstructor @MappedSuperclass
public abstract class AbstractSale extends AbstractIdentifiable implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @JoinColumn(name=COLUMN_CUSTOMER) protected Customer customer;
	@ManyToOne @JoinColumn(name=COLUMN_SALABLE_PRODUCT_COLLECTION) protected SalableProductCollection salableProductCollection;
	
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
	
	public static final String COLUMN_CUSTOMER = FIELD_CUSTOMER;
	public static final String COLUMN_SALABLE_PRODUCT_COLLECTION = FIELD_SALABLE_PRODUCT_COLLECTION;
	
	/**/
	
	@Getter @Setter
	public static class SearchCriteria extends DefaultSearchCriteria implements Serializable {

		private static final long serialVersionUID = 6796076474234170332L;

		private Collection<Customer> customers = new ArrayList<>();
		private Boolean hasAtLeastOneCashRegisterMovement;
		
		public SearchCriteria(){
			this(null,null);
		}
		
		public SearchCriteria(Date fromDate,Date toDate) {
			super(fromDate,toDate);
		}
		
		public SearchCriteria(SearchCriteria criteria){
			super(criteria);
			customers = new ArrayList<>(criteria.customers);
			hasAtLeastOneCashRegisterMovement = criteria.hasAtLeastOneCashRegisterMovement;
		}
		
		public SearchCriteria(DefaultSearchCriteria criteria){
			super(criteria);
		}
		
	}

	
}
