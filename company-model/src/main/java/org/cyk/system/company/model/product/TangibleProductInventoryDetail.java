package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;
import org.cyk.utility.common.annotation.user.interfaces.InputTextarea;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
public class TangibleProductInventoryDetail extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull
	@Input @InputChoice @InputOneChoice @InputOneCombo
	private TangibleProduct tangibleProduct;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	@Input @InputNumber
	private BigDecimal quantity = BigDecimal.ZERO;
	
	@Column(length=1024*1)
	@Input @InputTextarea
	private String comments;
	
}
