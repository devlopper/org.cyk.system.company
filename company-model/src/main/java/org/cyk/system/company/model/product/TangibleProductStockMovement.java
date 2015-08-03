package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
@ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.MALE)
public class TangibleProductStockMovement extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull
	@Input @InputChoice @InputOneChoice @InputOneCombo
	private TangibleProduct tangibleProduct;
	
	@Temporal(TemporalType.TIMESTAMP) @Column(nullable=false) @NotNull(groups=org.cyk.utility.common.validation.System.class)
	private Date date;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	@Input @InputNumber
	private BigDecimal quantity = BigDecimal.ZERO;
	
	@Column(length=1024*1)
	//@Input @InputTextarea
	private String comments;
	
	/**/
	
	public static final String FIELD_TANGIBLE_PRODUCT = "tangibleProduct";
	public static final String FIELD_DATE = "date";
	public static final String FIELD_QUANTITY = "quantity";
	public static final String FIELD_COMMENTS = "comments";
	
}
