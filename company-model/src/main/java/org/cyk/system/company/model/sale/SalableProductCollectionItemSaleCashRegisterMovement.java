package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.Balance;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.MALE)
public class SalableProductCollectionItemSaleCashRegisterMovement extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull private SalableProductCollectionItem salableProductCollectionItem;
	@ManyToOne @NotNull private SaleCashRegisterMovement saleCashRegisterMovement;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal amount = BigDecimal.ZERO;
	@Embedded private Balance balance = new Balance();
	
	public SalableProductCollectionItemSaleCashRegisterMovement(SalableProductCollectionItem salableProductCollectionItem,SaleCashRegisterMovement saleCashRegisterMovement) {
		super();
		this.salableProductCollectionItem = salableProductCollectionItem;
		this.saleCashRegisterMovement = saleCashRegisterMovement;
	}
	
	/**/
	
	
	public static final String FIELD_SALABLE_PRODUCT_COLLECTION_ITEM = "salableProductCollectionItem";
	public static final String FIELD_SALE_CASH_REGISTER_MOVEMENT = "saleCashRegisterMovement";
	public static final String FIELD_AMOUNT = "amount";
	public static final String FIELD_BALANCE = "balance";
	
}
