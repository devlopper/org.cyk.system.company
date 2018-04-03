package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.cyk.system.company.model.payment.BalanceType;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
@ModelBean(genderType=GenderType.FEMALE,crudStrategy=CrudStrategy.BUSINESS)
public class Sale extends AbstractIdentifiable implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;

	@Deprecated
	@ManyToOne @JoinColumn(name=COLUMN_CUSTOMER) protected Customer customer;
	@Deprecated @ManyToOne @JoinColumn(name=COLUMN_SALABLE_PRODUCT_COLLECTION) protected SalableProductCollection salableProductCollection;
	@ManyToOne @JoinColumn(name=COLUMN_SALABLE_PRODUCT_STORE_COLLECTION) protected SalableProductStoreCollection salableProductStoreCollection;
	
	@Transient @Accessors(chain=true) private MovementCollection balanceMovementCollection;
	
	public Sale setBalanceMovementCollectionValue(BigDecimal value){
		balanceMovementCollection.setValue(value);
		return this;
	}
	
	/**/
	
	@Deprecated
	public static final String FIELD_CUSTOMER = "customer";
	@Deprecated public static final String FIELD_SALABLE_PRODUCT_COLLECTION = "salableProductCollection";
	public static final String FIELD_SALABLE_PRODUCT_STORE_COLLECTION = "salableProductStoreCollection";
	public static final String FIELD_BALANCE_MOVEMENT_COLLECTION = "balanceMovementCollection";
	
	/**/
	
	@Deprecated
	public static final String COLUMN_CUSTOMER = FIELD_CUSTOMER;
	@Deprecated public static final String COLUMN_SALABLE_PRODUCT_COLLECTION = FIELD_SALABLE_PRODUCT_COLLECTION;
	public static final String COLUMN_SALABLE_PRODUCT_STORE_COLLECTION = FIELD_SALABLE_PRODUCT_STORE_COLLECTION;
	
	/**/
	
	@Getter @Setter
	public static class SearchCriteria extends AbstractSale.SearchCriteria implements Serializable {

		private static final long serialVersionUID = 6796076474234170332L;

		private Collection<BalanceType> balanceTypes = new ArrayList<>();
		private Boolean hasAtLeastOneCashRegisterMovement;
		
		public SearchCriteria(){
			this(null,null,null);
		}
		
		public SearchCriteria(Date fromDate,Date toDate) {
			this(fromDate,toDate,null);
		}
		
		public SearchCriteria(Date fromDate,Date toDate,BalanceType balanceType) {
			super(fromDate,toDate);
			if(balanceType!=null)
				this.balanceTypes.add(balanceType);
		}
	}

}
