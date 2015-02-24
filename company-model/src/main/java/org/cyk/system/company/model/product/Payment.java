package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
public class Payment extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@Column(unique=true)
	private String identificationNumber;
	
	@ManyToOne
	private Sale sale;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false)
	private BigDecimal amountIn = BigDecimal.ZERO;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false)
	private BigDecimal amountOut = BigDecimal.ZERO;
	
	/**
	 * The amount paid
	 */
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false)
	private BigDecimal amountPaid = BigDecimal.ZERO;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date date;
	
	
	
}
