package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.payment.BalanceType;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
@ModelBean(genderType=GenderType.FEMALE,crudStrategy=CrudStrategy.BUSINESS)
public class Sale extends AbstractSale implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@Embedded private Balance balance = new Balance();
	
	/**/
	
	@Transient private Collection<SaleCashRegisterMovement> saleCashRegisterMovements = new ArrayList<>();
	
	/**/
	
	public static final String FIELD_BALANCE = "balance";
	public static final String FIELD_CASH_REGISTER_MOVEMENT_TERM_COLLECTION = "cashRegisterMovementTermCollection";
	
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
