package org.cyk.system.company.model.payment;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractCollectionItem;
import org.cyk.system.root.model.event.Event;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
@ModelBean(crudStrategy=CrudStrategy.BUSINESS) @Deprecated
public class CashRegisterMovementTerm extends AbstractCollectionItem<CashRegisterMovementTermCollection> implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal amount;
	@OneToOne private Event event;
	
	public static final String FIELD_EVENT = "event";
	public static final String FIELD_AMOUNT = "amount";
	
}
