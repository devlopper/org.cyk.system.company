package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;

@Getter @Setter @NoArgsConstructor @Entity
public class Payment extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne
	private Sale sale;
	
	/**
	 * The amount given as input
	 */
	@Column(precision=10,scale=FLOAT_SCALE)
	private BigDecimal amountPaid;
	
	/**
	 * The amount given back to the hand
	 */
	@Column(precision=10,scale=FLOAT_SCALE)
	private BigDecimal amountHanded;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	
}
