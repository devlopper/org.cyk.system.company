package org.cyk.system.company.model.payment;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Entity
@ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class PaymentTermCollection extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal amount;
	
}
