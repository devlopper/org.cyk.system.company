package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.Balance;
import org.cyk.system.root.model.AbstractCollectionItem;
import org.cyk.system.root.model.IdentifiableRuntimeCollection;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.MALE)
public class SaleCashRegisterMovement extends AbstractCollectionItem<SaleCashRegisterMovementCollection> implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull private Sale sale;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal amount = BigDecimal.ZERO;
	
	@Embedded private Balance balance = new Balance();
	
	@Transient private IdentifiableRuntimeCollection<SalableProductCollectionItemSaleCashRegisterMovement> salableProductCollectionItemSaleCashRegisterMovements
		= new IdentifiableRuntimeCollection<>();

	/**/
	
	public Balance getBalance(){
		if(balance == null)
			balance =  new Balance();
		return this.balance;
	}
	
	/**/
	
	public static final String FIELD_SALE = "sale";
	public static final String FIELD_AMOUNT = "amount";
	public static final String FIELD_BALANCE = "balance";

}
